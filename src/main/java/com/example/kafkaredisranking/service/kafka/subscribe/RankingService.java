package com.example.kafkaredisranking.service.kafka.subscribe;

import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.repository.UserRepository;
import com.example.kafkaredisranking.service.kafka.subscribe.util.KafkaMessageParser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RankingService {

    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @Autowired
    public RankingService(RedisTemplate<String, String> redisTemplate, UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    @PostConstruct // 모든 설정(Bean)이 완료되면 호출
    public void init() {
        Iterable<User> users = userRepository.findAll();

        // Sorted Set에 작업을 수행할 수 있는 인터페이스를 반환
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        for (User user : users) {
            zSetOperations.add("userScores", user.getUserId(), user.getTotalScore());
        }
    }

    @KafkaListener(topics = "gameScores", groupId = "ranking-group-id")
    public void updateRanking(String message) {
        System.out.println("Ranking received message: " + message);
        String[] parseMessage = KafkaMessageParser.parseFromMessage(message);
        String userId = Objects.requireNonNull(parseMessage)[0];
        int score = Integer.parseInt(parseMessage[1]);

        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("userScores", userId, score);
    }
}
