package com.krainet.auth.core.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role {

    ADMIN,
    USER;

    public String getRoleWithPrefix() {
        return "ROLE_" + this.name();
    }
    public String getRoleWithoutPrefix() {
        return name();
    }
}