package com.example.kafkaredisranking.repository;

import com.example.kafkaredisranking.entity.GamePlayStats;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
public class GamePlayStatsRepositoryTest {

    @Autowired
    private GamePlayStatsRepository gamePlayStatsRepository;

    @Test
    public void whenSaveGamePlayStats_thenOK() {
        // given
        GamePlayStats gamePlayStats = GamePlayStats.builder()
                .id(1L)
                .userId("minjun")
                .playTime(LocalDateTime.now())
                .build();

        // when
        GamePlayStats savedGamePlayStats = gamePlayStatsRepository.save(gamePlayStats);

        // then
        Assertions.assertThat(savedGamePlayStats).isNotNull();
        Assertions.assertThat(savedGamePlayStats.getId()).isEqualTo(gamePlayStats.getId());
        Assertions.assertThat(savedGamePlayStats.getUserId()).isEqualTo(gamePlayStats.getUserId());
        Assertions.assertThat(savedGamePlayStats.getPlayTime()).isEqualTo(gamePlayStats.getPlayTime());
    }
}
