package com.example.kafkaredisranking.controller;

import com.example.kafkaredisranking.dto.UserDTO;
import com.example.kafkaredisranking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserScoreController.class)
public class UserScoreControllerTest {

    @Autowired //Spring의 의존성 주입 기능을 사용하여 빈을 자동으로 연결
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO(1L, "minjun", "민준", 100, 2024);
    }

    @Test
    public void saveUserTest() throws Exception {
        // given is in setUp()

        // when & then
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());

        verify(userService, times(1)).saveUser(any(UserDTO.class));
    }

    @Test
    public void addUserScoreTest() throws Exception {
        // given is in setUp()

        // when & then
        mockMvc.perform(post("/api/user/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());

        verify(userService, times(1)).addScoreByUserId(any(UserDTO.class));
    }
}
