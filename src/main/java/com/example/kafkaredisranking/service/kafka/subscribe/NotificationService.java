package com.example.kafkaredisranking.service.kafka.subscribe;

import com.example.kafkaredisranking.service.kafka.subscribe.util.KafkaMessageParser;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    @KafkaListener(topics = "playTimes", groupId = "notification-group-id")
    public void sendNotification(String message) {
        String[] parsedMessage = KafkaMessageParser.parseFromMessage(message);
        sendPlayTimeNotification(parsedMessage);
    }

    private void sendPlayTimeNotification(String[] parsedMessage) {
        String userName = parsedMessage[0];
        int playCount = Integer.parseInt(parsedMessage[1]);
        System.out.println(userName + "님! 장시간 게임을 즐기셨으니 잠시 휴식시간을 갖어보는게 어떨까요?");
        System.out.println("현재 플레이 횟수는 " + playCount + "회 입니다.");
    }
}
