package com.krainet.auth.core.models.kafka;

import java.util.Set;

public record NotificationMessage(
        Set<String> to, String subject, String message){
}
