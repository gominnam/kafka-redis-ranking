package com.example.kafkaredisranking.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomExceptionTest {
    @Test
    void customException_defaultStatusCode() {
        CustomException ex = new CustomException(ErrorCode.BAD_REQUEST);
        assertEquals(400, ex.getErrorCode().getCode());
        assertEquals("잘못된 요청입니다.", ex.getMessage());
    }

    @Test
    void customException_customStatusCode() {
        CustomException ex = new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        assertEquals(1003, ex.getErrorCode().getCode());
        assertEquals("서버 내부 오류가 발생했습니다.", ex.getMessage());
    }
}
