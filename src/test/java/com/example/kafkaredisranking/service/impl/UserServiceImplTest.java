package com.example.kafkaredisranking.service.impl;

import com.example.kafkaredisranking.dto.UserDTO;
import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.repository.UserRepository;
import com.example.kafkaredisranking.service.kafka.publish.score.ScoreUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp(){
        userDTO = new UserDTO(1L, "minjun", "민준", 100, 2024);
    }

    @Test
    void whenSaveUser_then_publishEvent() {
        // given
        User user = User.builder()
                        .userName(userDTO.getUserName())
                        .totalScore(userDTO.getScore())
                        .build();

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.saveUser(userDTO);

        // then
        verify(userRepository, times(1)).save(user);
        verify(eventPublisher, times(1)).publishEvent(any(ScoreUpdatedEvent.class));
    }

    @Test
    void whenAddScoreByUserId_then_publishEvent() {
        // given
        String userId = userDTO.getUserId();
        int score = userDTO.getScore();

        when(userRepository.addScoreByUserId(userId, score)).thenReturn(1);
        when(userRepository.getScoreByUserId(userId)).thenReturn(2124);

        // when
        userService.addScoreByUserId(userDTO);

        // then
        verify(userRepository, times(1)).addScoreByUserId(userId, score);
        verify(eventPublisher, times(1)).publishEvent(any(ScoreUpdatedEvent.class));
    }

    @Test
    void  whenGetUserScore_then_returnTotalScore(){
        // given
        User user = User.builder()
                        .userId(userDTO.getUserId())
                        .userName(userDTO.getUserName())
                        .totalScore(userDTO.getTotalScore())
                        .build();

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.getScoreByUserId(user.getUserId())).thenReturn(user.getTotalScore());

        // when
        int result = userService.getUserScore(userDTO);

        // then
        assertThat(result).isEqualTo(2024);
        verify(userRepository, times(1)).getScoreByUserId(user.getUserId());
    }
}
