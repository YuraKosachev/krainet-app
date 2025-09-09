package com.krainet.auth.services;

import com.krainet.auth.core.constants.ClaimConstants;
import com.krainet.auth.core.enums.AccountStatus;
import com.krainet.auth.core.enums.TokenStatus;
import com.krainet.auth.core.enums.TokenType;
import com.krainet.auth.core.exceptions.BlockedStatusException;
import com.krainet.auth.core.exceptions.ValidationTokenException;
import com.krainet.auth.core.interfaces.services.JwtService;
import com.krainet.auth.core.mappers.TokenMapper;
import com.krainet.auth.core.models.dtos.AccountDto;
import com.krainet.auth.core.models.dtos.AuthorizationToken;
import com.krainet.auth.core.models.dtos.TokenDto;
import com.krainet.auth.core.models.dtos.ValidationResult;
import com.krainet.auth.core.models.entities.Account;
import com.krainet.auth.core.models.entities.Token;
import com.krainet.auth.core.utils.DateTimeUtils;
import com.krainet.auth.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    final int ONE_HOUR = 1;
    final int ONE_DAY = 1;

    @Value("${security.jwt.access-token.secret-key}")
    private String accessTokenSecret;

    @Value("${security.jwt.access-token.expired}")
    private Integer accessTokenExpiresIn;

    @Value("${security.jwt.refresh-token.secret_key}")
    private String refreshTokenSecret;

    @Value("${security.jwt.refresh-token.expired}")
    private Integer refreshTokenExpiresIn;

    @Value("${security.jwt.issuer}")
    private String issuer;

    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    private Key getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private String generateJwt(String subject,
                               String issuer,
                               Date expiration,
                               String secret,
                               Map<String, Object> claims ) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .expiration(expiration)
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractClaims(String token, String secret) {
        return Jwts.parser().setSigningKey(getSigningKey(secret)).build().parseClaimsJws(token).getBody();
    }

    private TokenDto generateAccessToken(String subject, Map<String, Object> claims) {
        var expiration = new Date(System.currentTimeMillis() + accessTokenExpiresIn);
        var token = TokenDto.builder()
                .expires(DateTimeUtils.toLocalDateTime(expiration))
                .token(generateJwt(subject, issuer, expiration, accessTokenSecret, claims));
        return token.build();
    }

    private TokenDto generateRefreshToken(String subject, Map<String, Object> claims) {
        var expiration = new Date(System.currentTimeMillis() + refreshTokenExpiresIn);
        var token = TokenDto.builder()
                .expires(DateTimeUtils.toLocalDateTime(expiration))
                .token(generateJwt(subject,issuer, expiration, refreshTokenSecret, claims));
        return token.build();
    }

    @Override
    public AuthorizationToken createAuthorizationToken(AccountDto accountDto) {

        Map<String, Object> claims = Map.of(
                ClaimConstants.ACCOUNT_ID, accountDto.getId(),
                ClaimConstants.ACCOUNT_LOGIN, accountDto.getLogin(),
                ClaimConstants.ACCOUNT_EMAIL, accountDto.getEmail(),
                ClaimConstants.ACCOUNT_FIRSTNAME, accountDto.getFirstName(),
                ClaimConstants.ACCOUNT_LASTNAME, accountDto.getLastName());

        var tokens = tokenRepository.getTokensByAccountId(accountDto.getId());
        var activeToken = tokens.stream().filter(t->t.getStatus() == TokenStatus.ACTIVE
                && ChronoUnit.HOURS.between( LocalDateTime.now(), t.getAccessTokenExpires()) > ONE_HOUR
                && ChronoUnit.DAYS.between( LocalDateTime.now(), t.getRefreshTokenExpires()) > ONE_DAY).findFirst();

        if(activeToken.isPresent())
        {
            return  tokenMapper.entityToDto(activeToken.get());
        }
        setTokensStatus(tokens, TokenStatus.INACTIVE);
        var newToken = createNewToken(accountDto.getLogin(), accountDto.getId(), claims);
        return tokenMapper.entityToDto(newToken);
    }
    @Override
    public AuthorizationToken refreshAuthorizationToken(String refreshToken) {
        Token token = tokenRepository.findTokenByRefreshToken(refreshToken).orElseThrow();

        if(ChronoUnit.HOURS.between( LocalDateTime.now(), token.getAccessTokenExpires()) > ONE_HOUR
                && ChronoUnit.DAYS.between( LocalDateTime.now(), token.getRefreshTokenExpires()) > ONE_DAY){
            return  tokenMapper.entityToDto(token);
        }

        Map<String, Object> claims = Map.of(
                ClaimConstants.ACCOUNT_ID, token.getAccount().getId(),
                ClaimConstants.ACCOUNT_LOGIN, token.getAccount().getUsername());

        if(ChronoUnit.DAYS.between( LocalDateTime.now(), token.getRefreshTokenExpires()) <= ONE_DAY){
            token.setStatus(TokenStatus.INACTIVE);
            tokenRepository.save(token);
            return tokenMapper.entityToDto(createNewToken(token.getAccount().getUsername(), token.getAccount().getId(), claims));
        }
        var accessToken = generateAccessToken(token.getAccount().getUsername(), claims);
        token.setAccessTokenExpires(accessToken.getExpires());
        token.setAccessToken(accessToken.getToken());
        return tokenMapper.entityToDto(tokenRepository.save(token));
    }

    private void setTokensStatus(List<Token> tokens, TokenStatus status) {
        tokens.stream().forEach(token -> token.setStatus(status));
        tokenRepository.saveAll(tokens);
    }

    private Token createNewToken(String subject, UUID accountId, Map<String, Object> claims) {

        var accessToken = generateAccessToken(subject, claims);
        var refreshToken = generateRefreshToken(subject, claims);

        var newToken = tokenRepository.save(tokenMapper.dtoToEntity(accessToken,
                refreshToken,
                Account.builder().id(accountId).build(),
                TokenStatus.ACTIVE));
        return newToken;
    }

    @Override
    public ValidationResult validateToken(String token, TokenType type) {
        var builder = ValidationResult.builder();
        if(token.isEmpty() || token.isBlank())
        {
            return builder.valid(false).message("Token is empty or null").build();
        }
        if(isTokenExpired(token, type)){
            return builder.valid(false).message("Token expired").build();
        }
        return builder.valid(true).build();
    }

    @Override
    public UserDetails getUserDetailsByToken(String token, TokenType type) {
        String username = extractUsername(token, type);
        if(username.isEmpty() || username.isBlank()){
            throw new ValidationTokenException("username is empty or null");
        }

        var tokenEntity = tokenRepository.findTokenByAccessToken(token)
                .orElseThrow(()-> new ValidationTokenException("token is invalid"));

        var account = tokenEntity.getAccount();

        if(account.getStatus() == AccountStatus.BLOCKED){
            throw new BlockedStatusException("account is blocked");
        }

        return account;
    }

    @Override
    public <T> T extractValue(String token, TokenType type, Function<Claims, T> claimsResolver) {
        String secret = type == TokenType.ACCESS_TOKEN ? accessTokenSecret : refreshTokenSecret;
        return extractClaims(token, secret, claimsResolver);
    }

    @Override
    public void changeTokenStatus(UUID accountId, TokenStatus status) {
        var tokens = tokenRepository.getTokensByAccountId(accountId);
        setTokensStatus(tokens, status);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        String secret = type == TokenType.ACCESS_TOKEN ? accessTokenSecret : refreshTokenSecret;
        return extractClaims(token, secret, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, String secret, Function<Claims,T> claimsResolver) {
        final Claims claims = extractClaims(token, secret);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token, TokenType type) {
        String secret = type  == TokenType.ACCESS_TOKEN ? accessTokenSecret : refreshTokenSecret;
        return extractClaims(token, secret, Claims::getSubject);
    }

}
