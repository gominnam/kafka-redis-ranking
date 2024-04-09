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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserStatsService {

    private final GamePlayStatsRepository statsRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserStatsService(GamePlayStatsRepository statsRepository, UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.statsRepository = statsRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    private static final long GAME_PLAY_COUNT_THRESHOLD = 8;

    @KafkaListener(topics = "gameScores", groupId = "user-stat-group-id")
    @Transactional
    public void updateUserStats(String message) {
        System.out.println("UserStats received message: " + message);
        String[] parsedMessage = KafkaMessageParser.parseFromMessage(message);
        GamePlayStats stats = saveStats(parsedMessage);
        checkAndPublishEvent(stats);
    }

    private GamePlayStats saveStats(String[] parsedMessage) {
        String userId = parsedMessage[0];
        User user = userRepository.findByUserIdWithStats(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GamePlayStats stats = new GamePlayStats();
        stats.setUser(user);
        stats.setPlayTime(LocalDateTime.now());
        return statsRepository.save(stats);
    }

    private void checkAndPublishEvent(GamePlayStats stats) {
        User user = stats.getUser();
        long playCount = user.countTodayGamePlays();
        if (isThresholdExceeded(playCount)) {
            publishEvent(user, playCount);
        }
    }

    private boolean isThresholdExceeded(long playCount) {
        return playCount > GAME_PLAY_COUNT_THRESHOLD && 1 == playCount % GAME_PLAY_COUNT_THRESHOLD;
    }

    private void publishEvent(User user, long playCount) {
        eventPublisher.publishEvent(new GamePlayTimeExceededEvent(this, user.getUserName(), playCount));
    }
}
