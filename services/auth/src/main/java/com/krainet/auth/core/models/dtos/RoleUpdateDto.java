package com.krainet.auth.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krainet.auth.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoleUpdateDto(
        @NotNull(message = "id is required")
        @JsonProperty("account_id")
        UUID accountId,

        @NotNull (message = "lastname is required")
        Role role
) {
}
