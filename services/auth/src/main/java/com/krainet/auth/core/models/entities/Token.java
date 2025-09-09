package com.krainet.auth.core.models.entities;

import com.krainet.auth.core.constants.DbConstants;
import com.krainet.auth.core.enums.TokenStatus;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = DbConstants.TOKEN_TABLE_NAME)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account;

    @Column(nullable = false, name = "access_token", columnDefinition = "TEXT")
    String accessToken;

    @Column(nullable = false, name = "refresh_token", columnDefinition = "TEXT")
    String refreshToken;

    @Column(nullable = false, name = "access_token_expires")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime accessTokenExpires;

    @Column(nullable = false, name = "refresh_token_expires")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime refreshTokenExpires;

    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    @Enumerated(EnumType.ORDINAL)
    TokenStatus status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    LocalDateTime created;
}

