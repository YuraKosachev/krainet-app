package com.krainet.notification.core.interfaces.services;

public interface EmailService {
    void send(String to, String subject, String text);
}
