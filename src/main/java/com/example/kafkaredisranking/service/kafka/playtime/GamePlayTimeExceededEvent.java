package com.example.kafkaredisranking.service.kafka.playtime;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@Setter
public class GamePlayTimeExceededEvent extends ApplicationEvent {
    private String userName;
    private int playCount;

    public GamePlayTimeExceededEvent(Object source) {
        super(source);
    }

    public GamePlayTimeExceededEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public GamePlayTimeExceededEvent(Object source, String userName, int playCount) {
        super(source);
        this.userName = userName;
        this.playCount = playCount;
    }
}
