package com.example.kafkaredisranking.service.kafka.subscribe;

import com.example.kafkaredisranking.dto.UserPlayCountDTO;
import com.example.kafkaredisranking.entity.GamePlayStats;
import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.repository.GamePlayStatsRepository;
import com.example.kafkaredisranking.repository.UserRepository;
import com.example.kafkaredisranking.service.kafka.subscribe.UserStatsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // spring container를 로드하지 않음
public class UserStatsServiceTest {

    @Mock
    private GamePlayStatsRepository statsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserStatsService userStatsService;

    @Test
    public void whenSaveStats_thenUserIsFoundAndStatsAreSaved() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        String[] parsedMessage = {"minjun"};
        User user = new User();
        user.setUserId("minjun");
        when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(user));
        when(statsRepository.save(any(GamePlayStats.class))).thenAnswer(i -> i.getArguments()[0]);

        Method method = UserStatsService.class.getDeclaredMethod("saveStats", String[].class);
        method.setAccessible(true);

        // when
        GamePlayStats stats = (GamePlayStats) method.invoke(userStatsService, (Object) parsedMessage);

        // then
        verify(userRepository).findByUserId(anyString());
        verify(statsRepository).save(any(GamePlayStats.class));
        assertNotNull(stats);
        assertEquals(user, stats.getUser());
    }

    @Test
    public void whenCheckAndPublishEvent_thenEventIsPublished() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        User user = new User();
        user.setUserId("minjun");
        GamePlayStats stats = new GamePlayStats();
        stats.setUser(user);
        stats.setPlayTime(LocalDateTime.now());

        UserPlayCountDTO userPlayCountDTO = UserPlayCountDTO.builder()
                        .userName("minjun")
                        .playCount(10)
                        .build();

        when(userRepository.findByUserIdAndCountPlayTimeForToday(anyString(), any(LocalDateTime.class))).thenReturn(Optional.of(userPlayCountDTO));

        Method method = UserStatsService.class.getDeclaredMethod("checkAndPublishEvent", GamePlayStats.class);
        method.setAccessible(true);

        // when
        method.invoke(userStatsService, stats);

        // then
        verify(userRepository).findByUserIdAndCountPlayTimeForToday(anyString(), any(LocalDateTime.class));
        verify(eventPublisher).publishEvent(any());
    }
}
