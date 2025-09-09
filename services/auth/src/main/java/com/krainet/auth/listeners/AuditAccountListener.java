package com.krainet.auth.listeners;

import com.krainet.auth.core.models.entities.Account;
import com.krainet.auth.core.models.entities.EventLog;
import com.krainet.auth.repositories.EventLogRepository;
import jakarta.persistence.*;
import org.springframework.context.annotation.Lazy;

public class AuditAccountListener {

    private final EventLogRepository eventLogRepository;
    public AuditAccountListener(@Lazy EventLogRepository eventLogRepository) {
        this.eventLogRepository= eventLogRepository;
    }

    @PrePersist
    public void prePersist(Account account) {
        var subject = String.format("Создан пользователь %s", account.getUsername());
        var message = String.format("Создан пользователь с именем - %s, хэш - %s и почтой - %s", account.getUsername(), account.getPassword(), account.getMail());
        sendNotification(subject, message);
    }

    @PreUpdate
    public void preUpdate(Account account) {
        var subject = String.format("Изменен пользователь %s", account.getUsername());
        var message = String.format("Изменен пользователь с именем - %s, хэш - %s и почтой - %s", account.getUsername(), account.getPassword(), account.getMail());
        sendNotification(subject, message);
    }

    @PreRemove
    public void preRemove(Account account) {
        var subject = String.format("Удален пользователь %s", account.getUsername());
        var message = String.format("Удален пользователь с именем - %s, хэш - %s и почтой - %s", account.getUsername(), account.getPassword(), account.getMail());
        sendNotification(subject, message);
    }

    protected void sendNotification(String subject, String message) {
       var eventLog = EventLog.builder()
               .subject(subject)
               .description(message)
               .build();
       eventLogRepository.save(eventLog);
    }
}