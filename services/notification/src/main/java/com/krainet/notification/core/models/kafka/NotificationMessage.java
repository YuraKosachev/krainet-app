package com.krainet.notification.core.models.kafka;

import java.util.List;
import java.util.Set;

public record NotificationMessage(
        Set<String> to, String subject, String message){
}