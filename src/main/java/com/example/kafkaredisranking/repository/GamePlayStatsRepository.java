package com.example.kafkaredisranking.repository;

import com.example.kafkaredisranking.entity.GamePlayStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayStatsRepository extends JpaRepository<GamePlayStats, Long> {
    @Override
    GamePlayStats save(GamePlayStats gamePlayStats);
}
