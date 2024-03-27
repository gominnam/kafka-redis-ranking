package com.example.kafkaredisranking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String userId;
    private String userName;
    private int score;
    private int totalScore;
}
