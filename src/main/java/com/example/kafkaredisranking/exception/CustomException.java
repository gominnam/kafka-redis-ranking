package com.example.kafkaredisranking.exception;

public class CustomException extends RuntimeException {

    private final int statusCode;

    public CustomException(String message) {
        super(message);
        this.statusCode = 400; // 기본적으로 400(Bad Request) 코드
    }

    public CustomException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}