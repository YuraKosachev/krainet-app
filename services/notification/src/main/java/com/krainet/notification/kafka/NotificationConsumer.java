package com.krainet.notification.kafka;

import com.krainet.notification.core.constants.KafkaConstants;
import com.krainet.notification.core.models.entities.Notification;
import com.krainet.notification.core.models.kafka.NotificationMessage;
import com.krainet.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = KafkaConstants.NOTIFICATION_TOPIC)
    public void listen(NotificationMessage notificationMessage) {
        log.info("Received notification: {}", notificationMessage);
        try {

            var notification = Notification.builder()
                    .subject(notificationMessage.subject())
                    .emails(String.join(";",notificationMessage.to()))
                    .body(notificationMessage.message())
                    .build();
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.error("Error saving notification: {}", notificationMessage, e);
        }
    }

}
