package com.example.kafkaredisranking.service.impl;

import com.example.kafkaredisranking.dto.UserDTO;
import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.exception.CustomException;
import com.example.kafkaredisranking.exception.ErrorCode;
import com.example.kafkaredisranking.repository.UserRepository;
import com.example.kafkaredisranking.service.UserService;
import com.example.kafkaredisranking.service.kafka.publish.score.ScoreUpdatedEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(ApplicationEventPublisher eventPublisher, UserRepository userRepository, RedisTemplate<String, String> redisTemplate, ModelMapper modelMapper) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.modelMapper = modelMapper;
    }

    public void saveUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);
    }

    @Transactional
    public void addScoreByUserId(UserDTO userDTO) {
        String userId = userDTO.getUserId();
        int score = userDTO.getScore();

        int updatedRows = userRepository.addScoreByUserId(userId, score);
        if (updatedRows == 0) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND, "User not found");
        }

        int totalScore = userRepository.getScoreByUserId(userId);
        eventPublisher.publishEvent(new ScoreUpdatedEvent(this, userId, totalScore));
    }

    public int getUserScore(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return userRepository.getScoreByUserId(user.getUserId());
    }

    public List<UserDTO> getTopUsers(int top) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> topUsersWithScores = zSetOperations.reverseRangeWithScores("userScores", 0, top - 1);

        List<UserDTO> topUsers = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> userWithScore : topUsersWithScores) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userWithScore.getValue());
            userDTO.setScore(userWithScore.getScore().intValue());
            topUsers.add(userDTO);
        }

        return topUsers;
    }
}
