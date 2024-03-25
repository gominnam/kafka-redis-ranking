package com.example.kafkaredisranking.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

    @Test
    public void createUser() {
        // given
        User user = User.builder()
                .userId("minjun")
                .userName("민준")
                .totalScore(2024)
                .build();

        // when & then
        assertNotNull(user);
        assertEquals("minjun", user.getUserId());
        assertEquals("민준", user.getUserName());
        assertEquals(2024, user.getTotalScore());
    }
}
