package com.example.kafkaredisranking.service.subscribe;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

    @KafkaListener(topics = "gameScores", groupId = "rankingService")
    public void updateRanking(String scoreMessage) {
        // 점수 업데이트 로직
    }
}
