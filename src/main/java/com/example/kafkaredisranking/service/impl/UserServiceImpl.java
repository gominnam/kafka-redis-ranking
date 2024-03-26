package com.example.kafkaredisranking.service.impl;

import com.example.kafkaredisranking.dto.UserDTO;
import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.exception.CustomException;
import com.example.kafkaredisranking.repository.UserRepository;
import com.example.kafkaredisranking.service.UserService;
import com.example.kafkaredisranking.service.kafka.ScoreUpdatedEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(ApplicationEventPublisher eventPublisher, UserRepository userRepository, ModelMapper modelMapper) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void saveUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);

        int totalScore = userDTO.getScore();
        eventPublisher.publishEvent(new ScoreUpdatedEvent(this, user.getUserId(), totalScore));
    }

    @Transactional
    public void addScoreByUserId(UserDTO userDTO) {
        String userId = userDTO.getUserId();
        int score = userDTO.getScore();

        int updatedRows = userRepository.addScoreByUserId(userId, score);
        if (updatedRows == 0) {
            throw new CustomException("Score update failed for user: " + userId);
        }

        int totalScore = userRepository.getScoreByUserId(userId);
        eventPublisher.publishEvent(new ScoreUpdatedEvent(this, userId, totalScore));
    }

    public int getUserScore(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return userRepository.getScoreByUserId(user.getUserId());
    }
}
