package com.krainet.auth.services;

import com.krainet.auth.core.interfaces.services.EventLogService;
import com.krainet.auth.core.models.entities.EventLog;
import com.krainet.auth.repositories.EventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventLogServiceImpl implements EventLogService {
    private final EventLogRepository eventLogRepository;
    @Override
    @Transactional
    public void create(EventLog eventLog) {
        eventLogRepository.save(eventLog);
    }
}
