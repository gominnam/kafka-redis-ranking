package com.example.kafkaredisranking.service.kafka.publish.score;

import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScoreUpdatedEventListener {

    private static final String TOPIC_NAME = "gameScores";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ScoreUpdatedEventListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventListener
    public void onScoreUpdated(ScoreUpdatedEvent event) {
        kafkaTemplate.send(TOPIC_NAME, formatMessage(event));
    }

    private String formatMessage(ScoreUpdatedEvent event) {
        System.out.println("event.getUserId() + \":\" + event.getTotalScore() = " + event.getUserId() + ":" + event.getTotalScore());
        return event.getUserId() + ":" + event.getTotalScore();
    }
}
