package com.krainet.auth.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {
    ACTIVE("active", 0),
    BLOCKED("blocked", 1);
    private String label;
    private int value;
    public static AccountStatus getAccountStatus(String value) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.getLabel().equals(value)) {
                return status;
            }
        }
        throw new EnumConstantNotPresentException(AccountStatus.class, "%s not found".formatted(value));
    }

    public static AccountStatus getAccountStatus(int value) {
        for (AccountStatus status : AccountStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new EnumConstantNotPresentException(AccountStatus.class, "%s not found".formatted(value));
    }
}
