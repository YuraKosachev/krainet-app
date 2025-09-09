package com.krainet.auth.repositories;

import com.krainet.auth.core.enums.LogStatus;
import com.krainet.auth.core.models.entities.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventLogRepository extends JpaRepository<EventLog, UUID> {
    @Query("select l from EventLog l where l.status IN :statuses")
    List<EventLog> getLogsByStatuses(@Param("statuses") Set<LogStatus> statuses);
}
