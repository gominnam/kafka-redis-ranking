package com.example.kafkaredisranking.service.kafka.subscribe;

import com.example.kafkaredisranking.service.kafka.subscribe.util.KafkaMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RankingService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RankingService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "gameScores", groupId = "ranking-group-id")
    public void updateRanking(String message) {
        System.out.println("Ranking received message: " + message);
        String[] parseMessage = KafkaMessageParser.parseFromMessage(message);
        String userId = Objects.requireNonNull(parseMessage)[0];
        int score = Integer.parseInt(parseMessage[1]);

        // Sorted Set에 작업을 수행할 수 있는 인터페이스를 반환
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("userScores", userId, score);
    }
}
