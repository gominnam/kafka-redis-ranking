package com.example.kafkaredisranking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "user_id", unique = true, nullable = false, columnDefinition = "varchar(255)")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "total_score", columnDefinition = "integer default 0")
    private int totalScore;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GamePlayStats> gamePlayStats;
}
