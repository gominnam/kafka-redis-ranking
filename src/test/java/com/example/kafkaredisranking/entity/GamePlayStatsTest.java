package com.example.kafkaredisranking.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePlayStatsTest {

    @Test
    public void createGamePlayStats() {
        // when
         User testUser = User.builder()
                .id(1L)
                .userId("minjun")
                .userName("민준")
                .totalScore(2024)
                 .build();

        GamePlayStats gamePlayStats = GamePlayStats.builder()
                .id(1L)
                .user(testUser)
                .playTime(LocalDateTime.now())
                .build();

        // then
        assertEquals(1L, gamePlayStats.getId());
        assertEquals(testUser, gamePlayStats.getUser());
        assertEquals("minjun", gamePlayStats.getUser().getUserId());
    }
}
