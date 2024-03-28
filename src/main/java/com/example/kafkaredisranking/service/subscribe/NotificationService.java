package com.example.kafkaredisranking.service.subscribe;

import com.example.kafkaredisranking.service.subscribe.util.KafkaMessageParser;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private static final int PLAY_COUNT_THRESHOLD = 8;

    @KafkaListener(topics = "playTimes", groupId = "notificationService")
    public void sendNotification(String message) {
        String[] parsedMessage = KafkaMessageParser.parseFromMessage(message);
        if (shouldNotify(parsedMessage)) {
            sendPlayTimeNotification(parsedMessage);
        }
    }

    private boolean shouldNotify(String[] parsedMessage) {
        int playCount = Integer.parseInt(parsedMessage[1]);
        return playCount > 0 && playCount % PLAY_COUNT_THRESHOLD == 0;
    }

    private void sendPlayTimeNotification(String[] parsedMessage) {
        String userName = parsedMessage[0];
        int playCount = Integer.parseInt(parsedMessage[1]);
        System.out.println(userName + "님! 장시간 게임을 즐기셨으니 잠시 휴식시간을 갖어보는게 어떨까요?");
        System.out.println("현재 플레이 횟수는 " + playCount + "회 입니다.");
    }
}
