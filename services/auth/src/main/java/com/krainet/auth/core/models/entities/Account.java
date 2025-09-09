package com.krainet.auth.core.models.entities;

import com.krainet.auth.core.constants.DbConstants;
import com.krainet.auth.core.enums.AccountStatus;
import com.krainet.auth.core.enums.Role;
import com.krainet.auth.listeners.AuditAccountListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = DbConstants.ACCOUNT_TABLE_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditAccountListener.class)
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(unique = true, nullable = false)
    String mail;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false, name = "hashed_password", columnDefinition = "VARCHAR(60)")
    String password;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    @Enumerated(EnumType.ORDINAL)
    AccountStatus status;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Token> tokens;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleWithPrefix()));
    }

    @PrePersist
    protected void onCreate() {
        if(status == null) {
            status = AccountStatus.ACTIVE;
        }
        if(role == null) {
            role = Role.USER;
        }
    }

}
