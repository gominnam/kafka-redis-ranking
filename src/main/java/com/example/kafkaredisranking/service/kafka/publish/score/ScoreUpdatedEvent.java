package com.example.kafkaredisranking.service.kafka.publish.score;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@Setter
public class ScoreUpdatedEvent extends ApplicationEvent {
    private String userId;
    private int totalScore;

    public ScoreUpdatedEvent(Object source) {
        super(source); // source는 이벤트의 소스 객체 (ex. UserServiceImpl)
    }

    public ScoreUpdatedEvent(Object source, Clock clock) {
        super(source, clock); // clock은 이벤트의 타임스탬프 (logging, debugging 용도)
    }

    public ScoreUpdatedEvent(Object source, String userId, int totalScore) {
        super(source);
        this.userId = userId;
        this.totalScore = totalScore;
    }
}
