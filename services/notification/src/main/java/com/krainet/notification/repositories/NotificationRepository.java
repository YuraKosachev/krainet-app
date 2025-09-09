package com.krainet.notification.repositories;

import com.krainet.notification.core.enums.NotificationStatus;
import com.krainet.notification.core.models.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>{
    @Query("select n from Notification n where n.status IN :statuses")
    List<Notification> getAllByStatuses(@Param("statuses") Set<NotificationStatus> statuses);
}
