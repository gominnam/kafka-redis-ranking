package com.example.kafkaredisranking.service.kafka.subscribe;

import com.example.kafkaredisranking.entity.GamePlayStats;
import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.repository.GamePlayStatsRepository;
import com.example.kafkaredisranking.repository.UserRepository;
import com.example.kafkaredisranking.service.kafka.publish.playtime.GamePlayTimeExceededEvent;
import com.example.kafkaredisranking.service.kafka.subscribe.util.KafkaMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserStatsService {

    private GamePlayStatsRepository statsRepository;
    private UserRepository userRepository;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserStatsService(GamePlayStatsRepository statsRepository, UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.statsRepository = statsRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @KafkaListener(topics = "gameScores", groupId = "statsService")
    public void updateUserStats(String message) {
        String[] parsedMessage = KafkaMessageParser.parseFromMessage(message);
        GamePlayStats stats = saveStats(parsedMessage);
        checkAndPublishEvent(stats);
    }

    private GamePlayStats saveStats(String[] parsedMessage) {
        String userId = parsedMessage[0];
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));

        GamePlayStats stats = new GamePlayStats();
        stats.setUser(user);
        stats.setPlayTime(LocalDateTime.now());
        return statsRepository.save(stats);
    }

    private void checkAndPublishEvent(GamePlayStats stats) {
        userRepository.findByUserIdAndCountPlayTimeForToday(stats.getUser().getUserId(), stats.getPlayTime())
                .ifPresent(userPlayCountDTO ->
                        eventPublisher.publishEvent(new GamePlayTimeExceededEvent(this, userPlayCountDTO.getUserName(), userPlayCountDTO.getPlayCount()))
                );
    }
}
