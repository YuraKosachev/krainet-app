package com.krainet.auth.core.models.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDto {
    UUID id;
    String login;
    String firstName;
    String lastName;
    String email;
}

