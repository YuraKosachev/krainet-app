package com.krainet.auth.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenStatus {
    ACTIVE("active", 0),
    INACTIVE("in_active", 1);
    private String label;
    private int value;
    public static TokenStatus getTokenStatus(String value) {
        for (TokenStatus status : TokenStatus.values()) {
            if (status.getLabel().equals(value)) {
                return status;
            }
        }
        throw new EnumConstantNotPresentException(TokenStatus.class, "%s not found".formatted(value));
    }

    public static TokenStatus getTokenStatus(int value) {
        for (TokenStatus status : TokenStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new EnumConstantNotPresentException(TokenStatus.class, "%s not found".formatted(value));
    }
}