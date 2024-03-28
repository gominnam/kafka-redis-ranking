package com.example.kafkaredisranking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomExceptionTest {
    @Test
    void customException_defaultStatusCode() {
        CustomException ex = new CustomException("Default error message");
        assertEquals(400, ex.getStatusCode());
        assertEquals("Default error message", ex.getMessage());
    }

    @Test
    void customException_customStatusCode() {
        CustomException ex = new CustomException("Custom error message", 404);
        assertEquals(404, ex.getStatusCode());
        assertEquals("Custom error message", ex.getMessage());
    }
}
