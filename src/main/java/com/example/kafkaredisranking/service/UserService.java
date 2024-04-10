package com.example.kafkaredisranking.service;

import com.example.kafkaredisranking.dto.UserDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);
    int getUserScore(UserDTO userDTO);
    void addScoreByUserId(UserDTO userDTO);
    List<UserDTO> getTopUsers(int top);
}
