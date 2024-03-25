package com.example.kafkaredisranking.service.impl;

import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.repository.UserRepository;
import com.example.kafkaredisranking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(KafkaTemplate<String, String> kafkaTemplate, UserRepository userRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.userRepository = userRepository;
    }

    public void saveUserScore(String userId, String userName, int score) {
        userRepository.save(User.builder()
                .userId(userId)
                .userName(userName)
                .totalScore(score)
                .build());

        kafkaTemplate.send("gameScores", userId + ":" + score);
    }

    public void updateUserScore(String userId, int score) {
        userRepository.addScoreByUserId(userId, score);

        kafkaTemplate.send("gameScores", userId + ":" + score);
    }

    public int getUserScore(String userId) {
        return userRepository.getScoreByUserId(userId);
    }

}
