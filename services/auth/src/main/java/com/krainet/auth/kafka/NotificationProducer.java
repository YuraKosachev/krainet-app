package com.krainet.auth.kafka;

import com.krainet.auth.core.constants.KafkaConstants;
import com.krainet.auth.core.models.kafka.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void sendNotification(NotificationMessage message) {
        log.info("Sending notification");
        kafkaTemplate.send(getMessageBuilder(message,KafkaConstants.NOTIFICATION_TOPIC));

        log.info("Notification sent successfully");
    }
//    public void sendNotification(NotificationMessage message) {
//        log.info("Sending notification");
//        kafkaNotificationTemplate.send(getMessageBuilder(message, KafkaConstants.NOTIFICATION_TOPIC));
//        log.info("Notification sent");
//    }

    private <T> Message<T> getMessageBuilder(T message, String topic) {
        return MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }
}
