package com.example.kafkaredisranking.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String userName;
    private double score;
}
