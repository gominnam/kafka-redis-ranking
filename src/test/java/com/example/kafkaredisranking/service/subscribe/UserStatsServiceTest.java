package com.example.kafkaredisranking.service.subscribe;

import com.example.kafkaredisranking.entity.GamePlayStats;
import com.example.kafkaredisranking.repository.GamePlayStatsRepository;
import com.example.kafkaredisranking.service.subscribe.util.KafkaMessageParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserStatsServiceTest {

    @Mock
    private GamePlayStatsRepository statsRepository;

    @InjectMocks
    private UserStatsService userStatsService;

    @Test
    public void whenUpdateUserStats_thenSaveStats() {
        // given
        String message = "minjun:100";

        // when
        userStatsService.updateUserStats(message);

        // then
        verify(statsRepository).save(any(GamePlayStats.class));
    }
}
