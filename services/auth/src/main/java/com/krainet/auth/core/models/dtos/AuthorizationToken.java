package com.krainet.auth.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krainet.auth.core.constants.DateTimeConstants;

import java.time.LocalDateTime;

public record AuthorizationToken(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("expires_in")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeConstants.DATE_TIME_FORMAT)
        LocalDateTime expiresIn
) { }