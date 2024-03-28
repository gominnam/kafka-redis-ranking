package com.example.kafkaredisranking.service.subscribe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RankingServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplateMock;

    @Mock
    private ZSetOperations<String, String> zSetOperationsMock;

    @InjectMocks
    private RankingService rankingService;

    @BeforeEach
    public void setUp() {
        when(redisTemplateMock.opsForZSet()).thenReturn(zSetOperationsMock);
    }

    @Test
    public void whenUpdateRanking_thenAddToSortedSet() {
        // given
        String message = "minjun:100";

        // when
        rankingService.updateRanking(message);

        // then
        verify(redisTemplateMock, times(1)).opsForZSet();
        verify(zSetOperationsMock, times(1)).add(eq("userScores"), eq("minjun"), eq(100.0));
    }

}
