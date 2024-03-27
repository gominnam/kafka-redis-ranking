package com.example.kafkaredisranking.service.subscribe;

import com.example.kafkaredisranking.entity.GamePlayStats;
import com.example.kafkaredisranking.repository.GamePlayStatsRepository;
import com.example.kafkaredisranking.service.subscribe.util.KafkaMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserStatsService {

    private GamePlayStatsRepository statsRepository;

    @Autowired
    public UserStatsService(GamePlayStatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @KafkaListener(topics = "gameScores", groupId = "statsService")
    public void updateUserStats(String message) {
        String[] parseMessage = KafkaMessageParser.parseFromMessage(message);
        String userId = parseMessage[0];

        GamePlayStats stats = new GamePlayStats();
        stats.setUserId(userId);
        stats.setPlayTime(LocalDateTime.now());
        statsRepository.save(stats);
    }
}