package com.krainet.auth.core.exceptions;

public class BlockedStatusException extends RuntimeException {
    public BlockedStatusException(String message) {
        super(message);
    }
}
