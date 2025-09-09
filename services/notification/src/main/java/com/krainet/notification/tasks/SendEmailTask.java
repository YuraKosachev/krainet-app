package com.krainet.notification.tasks;

import com.krainet.notification.core.enums.NotificationStatus;
import com.krainet.notification.core.interfaces.services.EmailService;
import com.krainet.notification.repositories.NotificationRepository;
import com.krainet.notification.services.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendEmailTask {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;


    @Scheduled(cron = "${task.notification.cron}")
    public void sendEmail() {
        var notifications = notificationRepository.getAllByStatuses(Set.of(NotificationStatus.ERROR, NotificationStatus.NEW));
        log.info("Sending email");
        if(notifications.isEmpty()) {
            log.info("No notifications found");
            return;
        }
        try{
            notifications.forEach(notification -> {notification.setStatus(NotificationStatus.PROCCESSED);});
            notificationRepository.saveAll(notifications);

            notifications.forEach(notification -> {
                emailService.send(notification.getEmails(), notification.getSubject(), notification.getBody());
                notification.setStatus(NotificationStatus.SENT);
            });
            notificationRepository.saveAll(notifications);
            log.info(String.format("Notifications sent successfully. Total notifications: %d", notificationRepository.count()));
        }catch (Exception e){
            notifications.forEach(notification -> notification.setStatus(NotificationStatus.ERROR));
            notificationRepository.saveAll(notifications);
            log.error(e.getMessage());
        }
    }
}
