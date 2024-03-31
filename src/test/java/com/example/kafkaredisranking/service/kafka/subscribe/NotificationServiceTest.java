package com.example.kafkaredisranking.service.kafka.subscribe;

import com.example.kafkaredisranking.service.kafka.subscribe.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void whenShouldNotify_thenSendNotification() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        String[] parsedMessage1 = new String[]{"user", "8"};
        String[] parsedMessage2 = new String[]{"user", "7"};

        // when
        Method method = NotificationService.class.getDeclaredMethod("shouldNotify", String[].class);
        method.setAccessible(true);

        // then
        assertEquals(true, method.invoke(notificationService, (Object) parsedMessage1));
        assertNotEquals(true, method.invoke(notificationService, (Object) parsedMessage2));
    }

    @Test
    public void whenSendPlayTimeNotification_thenPrintMessage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        String[] parsedMessage = new String[]{"민준", "8"};

        // when
        Method method = NotificationService.class.getDeclaredMethod("sendPlayTimeNotification", String[].class);
        method.setAccessible(true);

        // then
        method.invoke(notificationService, (Object) parsedMessage);
    }
}
