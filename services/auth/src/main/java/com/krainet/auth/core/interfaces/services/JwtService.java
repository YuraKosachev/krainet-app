package com.krainet.auth.core.interfaces.services;

import com.krainet.auth.core.enums.TokenStatus;
import com.krainet.auth.core.enums.TokenType;
import com.krainet.auth.core.models.dtos.AccountDto;
import com.krainet.auth.core.models.dtos.AuthorizationToken;
import com.krainet.auth.core.models.dtos.ValidationResult;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;
import java.util.function.Function;

public interface JwtService {
    AuthorizationToken createAuthorizationToken(AccountDto accountDto);
    AuthorizationToken refreshAuthorizationToken(String refreshToken);
    UserDetails getUserDetailsByToken(String token, TokenType type);
    <T> T extractValue(String token, TokenType type, Function<Claims,T> claimsResolver);
    void changeTokenStatus(UUID accountId, TokenStatus status);
    ValidationResult validateToken(String token, TokenType type );
}