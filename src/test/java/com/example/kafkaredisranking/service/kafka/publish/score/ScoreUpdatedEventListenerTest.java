package com.example.kafkaredisranking.service.kafka.publish.score;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ScoreUpdatedEventListenerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private ScoreUpdatedEventListener scoreUpdatedEventListener;

    @Test
    public void onScoreUpdatedTest() {
        // given
        ScoreUpdatedEvent event = new ScoreUpdatedEvent(this);
        event.setUserId("user");
        event.setTotalScore(8);

        // when
        scoreUpdatedEventListener.onScoreUpdated(event);

        // then
        verify(kafkaTemplate).send("gameScores", "user:8");
    }
}
