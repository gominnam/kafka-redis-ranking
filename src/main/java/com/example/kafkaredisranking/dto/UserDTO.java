package com.example.kafkaredisranking.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String userId;
    private String userName;
    private int score;
    private int totalScore;
}
