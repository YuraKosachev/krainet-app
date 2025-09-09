package com.krainet.auth.core.mappers;

import com.krainet.auth.core.enums.TokenStatus;
import com.krainet.auth.core.models.dtos.AuthorizationToken;
import com.krainet.auth.core.models.dtos.TokenDto;
import com.krainet.auth.core.models.entities.Account;
import com.krainet.auth.core.models.entities.Token;
import org.springframework.stereotype.Component;

@Component
public final class TokenMapper {

    public Token dtoToEntity(TokenDto accessDto, TokenDto refreshDto, Account account, TokenStatus status) {
        var builder = Token.builder();

        builder.accessToken(accessDto.getToken())
                .refreshToken(refreshDto.getToken())
                .refreshTokenExpires(refreshDto.getExpires())
                .account(account)
                .status(status)
                .accessTokenExpires(accessDto.getExpires());

        return builder.build();
    }

    public AuthorizationToken entityToDto(Token entity) {
        return new AuthorizationToken(entity.getAccessToken(), entity.getRefreshToken(), entity.getAccessTokenExpires());
    }
}
