package com.example.kafkaredisranking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String userName;
    private int score;
    private int totalScore;
}
