package com.example.kafkaredisranking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionControllerTest {
    @GetMapping("/trigger-custom-exception")
    public ResponseEntity<Object> triggerException() {
        throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    @GetMapping("/trigger-general-exception")
    public ResponseEntity<Object> triggerGeneralException() {
        throw new RuntimeException("An unexpected error has occurred");
    }
}
