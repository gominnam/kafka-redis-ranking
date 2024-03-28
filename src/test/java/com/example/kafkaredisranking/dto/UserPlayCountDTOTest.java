package com.example.kafkaredisranking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserPlayCountDTOTest {

    @Test
    public void createUserPlayCountDTO() {
        UserPlayCountDTO userPlayCountDTO = UserPlayCountDTO.builder()
                .userName("민준검사")
                .playCount(10)
                .build();

        assertEquals("민준검사", userPlayCountDTO.getUserName());
        assertEquals(10, userPlayCountDTO.getPlayCount());
    }
}
