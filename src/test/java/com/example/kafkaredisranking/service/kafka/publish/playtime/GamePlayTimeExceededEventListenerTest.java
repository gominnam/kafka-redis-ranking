package com.example.kafkaredisranking.service.kafka.publish.playtime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class GamePlayTimeExceededEventListenerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private GamePlayTimeExceededEventListener gamePlayTimeExceededEventListener;

    @Test
    public void onGamePlayTimeExceededTest() {
        // given
        GamePlayTimeExceededEvent event = new GamePlayTimeExceededEvent(this);
        event.setUserName("user");
        event.setPlayCount(8);

        // when
        gamePlayTimeExceededEventListener.onGamePlayTimeExceeded(event);

        // then
         verify(kafkaTemplate).send("playTimes", "user:8");
    }
}
