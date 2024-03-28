package com.example.kafkaredisranking.repository;

import com.example.kafkaredisranking.dto.UserPlayCountDTO;
import com.example.kafkaredisranking.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenGetScoreByUserId_shouldReturnScore() {
        // given
        User user = User.builder()
                .id(1L)
                .userId("test")
                .userName("minjun")
                .totalScore(2024)
                .build();
        userRepository.save(user);

        // when
        Integer score = userRepository.getScoreByUserId(user.getUserId());
        if(score == null) {
            score = 0;
        }

        // then
        assertThat(score).isEqualTo(user.getTotalScore());
    }

    @Test
    public void whenAddScoreByUserId_shouldReturnNewScore() {
        // given
        User user = User.builder()
                .id(1L)
                .userId("test")
                .userName("minjun")
                .totalScore(2024)
                .build();
        userRepository.save(user);
        int score = 10;
        Integer newScore = user.getTotalScore()+score;

        // when
        userRepository.addScoreByUserId(user.getUserId(), score);

        // then
        assertThat(userRepository.getScoreByUserId(user.getUserId())).isEqualTo(newScore);
    }

    @Test
    public void whenFindByUserIdAndCountPlayTimeForToday_thenOK() {
        // given
        User user = User.builder()
                .id(1L)
                .userId("test")
                .userName("minjun")
                .totalScore(2024)
                .build();
        userRepository.save(user);

        // when
        Optional<UserPlayCountDTO> userPlayCountDTO = userRepository.findByUserIdAndCountPlayTimeForToday(user.getUserId(), LocalDateTime.now());

        // then
        if (userPlayCountDTO.isPresent()) {
            assertThat(userPlayCountDTO.get().getUserName()).isEqualTo(user.getUserName());
            assertThat(userPlayCountDTO.get().getPlayCount()).isEqualTo(1);
        }
    }
}
