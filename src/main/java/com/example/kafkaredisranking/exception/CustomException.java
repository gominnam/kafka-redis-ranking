package com.example.kafkaredisranking.exception;

import lombok.Getter;

public class CustomException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;
    private final String detailMessage;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = null;
    }

    public CustomException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage != null ? detailMessage : errorCode.getMessage();
    }
}