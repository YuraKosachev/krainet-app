package com.krainet.auth.tasks;

import com.krainet.auth.core.enums.LogStatus;
import com.krainet.auth.core.enums.Role;
import com.krainet.auth.core.models.entities.Account;
import com.krainet.auth.core.models.kafka.NotificationMessage;
import com.krainet.auth.kafka.NotificationProducer;
import com.krainet.auth.repositories.AccountRepository;
import com.krainet.auth.repositories.EventLogRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendNotificationTask {
    private final EventLogRepository eventLogRepository;
    private final AccountRepository accountRepository;
    private final NotificationProducer notificationProducer;

    @Scheduled(cron = "${task.notification.cron}")
    public void proccessNotification() {
        log.info("Processing notifications");
        var adminEmails = accountRepository.getAllByRole(Role.ADMIN)
                .stream().map(Account::getMail).collect(Collectors.toSet());
        var logs = eventLogRepository.getLogsByStatuses(Set.of(LogStatus.ERROR, LogStatus.NEW));
        if (adminEmails.isEmpty() || logs.isEmpty()) {
            return;
        }

        try {
            logs.forEach(log -> log.setStatus(LogStatus.PROCCESSING));
            eventLogRepository.saveAll(logs);

            logs.forEach((eventLog) -> {
                var message = new NotificationMessage(adminEmails, eventLog.getSubject(), eventLog.getDescription());
                notificationProducer.sendNotification(message);
                eventLog.setStatus(LogStatus.SENT);
            });

            eventLogRepository.saveAll(logs);
            log.info(String.format("Notifications sent successfully. Total notifications processed: %d", logs.size()));

        } catch (Exception e) {
            logs.forEach(log -> log.setStatus(LogStatus.ERROR));
            eventLogRepository.saveAll(logs);
            log.error(e.getMessage());
        }

    }
}
