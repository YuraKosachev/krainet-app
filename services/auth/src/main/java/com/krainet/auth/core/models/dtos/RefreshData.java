package com.krainet.auth.core.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshData(
        @NotNull(message = "token is required")
        @NotEmpty(message = "token is required")
        String refreshToken) { }
