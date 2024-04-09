package com.example.kafkaredisranking.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "잘못된 요청입니다."),
    INVALID_INPUT(1001, "입력 값이 유효하지 않습니다."),
    USER_NOT_FOUND(1002, "사용자를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(1003, "서버 내부 오류가 발생했습니다."),
    ACCESS_DENIED(1004, "접근이 거부되었습니다."),
    RESOURCE_NOT_FOUND(1005, "요청한 리소스를 찾을 수 없습니다.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
