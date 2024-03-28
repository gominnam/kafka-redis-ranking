package com.example.kafkaredisranking.repository;

import com.example.kafkaredisranking.dto.UserPlayCountDTO;
import com.example.kafkaredisranking.entity.GamePlayStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface GamePlayStatsRepository extends JpaRepository<GamePlayStats, Long> {
    @Override
    GamePlayStats save(GamePlayStats gamePlayStats);

//    @Query("SELECT g.userId, g.userName, count(g) as playCount FROM GamePlayStats g WHERE g.userId = ?1 AND Date(g.playTime) = CURRENT_DATE()")
//    Optional<UserPlayCountDTO> findByUserIdAndCountPlayTimeForToday(String userId, LocalDateTime playTime);
}
