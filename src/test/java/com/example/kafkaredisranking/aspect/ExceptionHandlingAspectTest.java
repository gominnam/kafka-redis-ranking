package com.example.kafkaredisranking.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlingAspectTest {

    @Mock
    private Logger logger;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private ExceptionHandlingAspect exceptionHandlingAspect;

    @Test
    public void handleServiceExceptionTest() throws Throwable {
        // given
        Signature signature = mock(Signature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Test exception"));

        // Create and start a ListAppender
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        // Get Logger and add ListAppender
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ExceptionHandlingAspect.class);
        logger.addAppender(listAppender);

        // when
        assertThrows(RuntimeException.class, () -> exceptionHandlingAspect.handleServiceException(joinPoint));

        // then
        boolean logged = listAppender.list.stream()
                .anyMatch(event -> event.getLevel() == Level.ERROR && event.getFormattedMessage().contains("Exception occurred in service method"));

        assertTrue(logged, "Expected error log not found");
    }
}
