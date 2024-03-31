package com.example.kafkaredisranking.service.kafka.subscribe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, topics = {"gameScores"})
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class RankingServiceTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Mock
    private RedisTemplate<String, String> redisTemplateMock;

    @Mock
    private ZSetOperations<String, String> zSetOperationsMock;

    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private RankingService rankingService;

    @BeforeEach
    public void setUp() {
        when(redisTemplateMock.opsForZSet()).thenReturn(zSetOperationsMock);

        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));
    }

    @Test
    public void whenUpdateRanking_thenAddToSortedSet() {
        // given
        String message = "minjun:100";

        // when
        kafkaTemplate.send("gameScores", message);
        rankingService.updateRanking(message);

        // then
        verify(redisTemplateMock, times(1)).opsForZSet();
        verify(zSetOperationsMock, times(1)).add(eq("userScores"), eq("minjun"), eq(100.0));
    }

}
