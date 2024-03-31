package com.example.kafkaredisranking.service.kafka.publish.playtime;

import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class GamePlayTimeExceededEventListener {

    private static final String TOPIC_NAME = "playTimes";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public GamePlayTimeExceededEventListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventListener
    public void onGamePlayTimeExceeded(GamePlayTimeExceededEvent event) {
        kafkaTemplate.send(TOPIC_NAME, formatMessage(event));
    }

    private String formatMessage(GamePlayTimeExceededEvent event) {
        return event.getUserName() + ":" + event.getPlayCount();
    }
}
