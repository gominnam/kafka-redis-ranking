package com.example.kafkaredisranking.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDTOTest {

    @Test
    public void createUserDTO() {
        // when
        UserDTO userDTO = UserDTO.builder()
                .userId("minjun")
                .userName("민준")
                .score(100)
                .build();

        // then
        assertEquals("minjun", userDTO.getUserId());
        assertEquals("민준", userDTO.getUserName());
        assertEquals(100, userDTO.getScore());
    }
}
