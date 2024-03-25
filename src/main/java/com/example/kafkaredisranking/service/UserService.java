package com.example.kafkaredisranking.service;

public interface UserService {
    void saveUserScore(String userId, String username, int score);
    int getUserScore(String userId);
    void updateUserScore(String userId, int score);
}
