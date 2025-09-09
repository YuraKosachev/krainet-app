package com.krainet.auth.core.models.entities;

import com.krainet.auth.core.constants.DbConstants;
import com.krainet.auth.core.enums.Event;
import com.krainet.auth.core.enums.LogStatus;
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
@Table(name = DbConstants.EVENT_LOG_TABLE_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = true)
    String subject;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    LogStatus status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    LocalDateTime created;

    @PrePersist
    protected void onCreate() {
        if(status == null) {
            status = LogStatus.NEW;
        }
    }
}
