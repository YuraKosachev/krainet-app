package com.krainet.notification.core.models.entities;

import com.krainet.notification.core.constants.DbConstants;
import com.krainet.notification.core.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = DbConstants.NOTIFICATION_TABLE_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, columnDefinition = "VARCHAR(2000)")
    String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    String body;

    @Column(nullable = false, columnDefinition = "TEXT")
    String emails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    NotificationStatus status;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if(status == null)
            status = NotificationStatus.NEW;
    }
}
