package com.example.kafkaredisranking.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamePlayStatsTest {

    @Test
    public void createGamePlayStats() {
        // when
        GamePlayStats gamePlayStats = GamePlayStats.builder()
                .id(1L)
                .userId("minjun")
                .playTime(LocalDateTime.now())
                .build();

        // then
        assertEquals(1L, gamePlayStats.getId());
        assertEquals("minjun", gamePlayStats.getUserId());
    }
}
