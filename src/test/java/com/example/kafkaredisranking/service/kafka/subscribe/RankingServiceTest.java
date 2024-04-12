package com.example.kafkaredisranking.service.kafka.subscribe;

import com.example.kafkaredisranking.entity.User;
import com.example.kafkaredisranking.repository.UserRepository;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

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
    private UserRepository userRepository;

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
    public void whenInit_thenAddToSortedSet() {
        // given
        User user1 = new User();
        user1.setUserId("user1");
        user1.setTotalScore(100);

        User user2 = new User();
        user2.setUserId("user2");
        user2.setTotalScore(200);

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // when
        rankingService.init();

        // then
        verify(zSetOperationsMock, times(1)).add("userScores", user1.getUserId(), user1.getTotalScore());
        verify(zSetOperationsMock, times(1)).add("userScores", user2.getUserId(), user2.getTotalScore());
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
