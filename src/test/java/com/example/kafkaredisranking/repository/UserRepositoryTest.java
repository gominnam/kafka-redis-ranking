package com.example.kafkaredisranking.repository;

import com.example.kafkaredisranking.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .userId("test")
                .userName("minjun")
                .totalScore(2024)
                .build();
    }

    @Test
    public void testGetScoreByUserId() {
        // given
        userRepository.save(user);

        // when
        int score = userRepository.getScoreByUserId(user.getUserId());

        // then
        assertThat(score).isEqualTo(user.getTotalScore());
    }

    @Test
    public void testAddScoreByUserId() {
        // given
        int score = 10;
        int newScore = user.getTotalScore()+score;
        userRepository.save(user);

        // when
        userRepository.addScoreByUserId(user.getUserId(), score);

        // then
        assertThat(userRepository.getScoreByUserId(user.getUserId())).isEqualTo(newScore);
    }
}
