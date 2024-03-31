package com.example.kafkaredisranking.service.kafka.subscribe.util;

public class KafkaMessageParser {
    public static String[] parseFromMessage(String message) {
        if(message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message is empty");
        }
        return message.split(":", -1);
    }

    public static String[] parseFromMessage(String message, String delimiter) {
        if(message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message is empty");
        }
        return message.split(delimiter, -1);
    }
}
