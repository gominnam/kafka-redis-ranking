package com.example.kafkaredisranking.exception.handler;

import com.example.kafkaredisranking.exception.ErrorCode;
import com.example.kafkaredisranking.exception.ExceptionControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionControllerTest.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenTriggerCustomException_thenRespondWithBadRequest() throws Exception {
        mockMvc.perform(get("/trigger-custom-exception"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.error").value("Custom Error"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.path").value(containsString("/trigger-custom-exception")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenTriggerGeneralException_thenRespondWithInternalServerError() throws Exception {
        mockMvc.perform(get("/trigger-general-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error has occurred"))
                .andExpect(jsonPath("$.path").value(containsString("/trigger-general-exception")))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
