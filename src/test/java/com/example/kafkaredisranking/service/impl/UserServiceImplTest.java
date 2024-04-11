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
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private RedisTemplate<String, String> redisTemplate;

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
    void whenGetUserScore_then_returnTotalScore(){
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

    @Test
    void whenGetTopUsers_then_returnTopUsers(){
        // given
        int top = 3;
        ZSetOperations<String, String> zSetOperations = mock(ZSetOperations.class);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        Set<ZSetOperations.TypedTuple<String>> topUsersWithScores = new HashSet<>();
        for (int i = 0; i < top; i++) {
            ZSetOperations.TypedTuple<String> userWithScore = new DefaultTypedTuple<>("user" + i, (double) ((top - i) * 100));
            topUsersWithScores.add(userWithScore);
        }

        when(zSetOperations.reverseRangeWithScores("userScores", 0, top - 1)).thenReturn(topUsersWithScores);

        // when
        List<UserDTO> result = userService.getTopUsers(top);

        // then
        assertThat(result.size()).isEqualTo(top);
        for (int i = 0; i < top; i++) {
            assertThat(result.get(i).getUserId()).isEqualTo("user" + (top - i - 1));
            assertThat(result.get(i).getScore()).isEqualTo((i + 1) * 100);
        }
    }
}
