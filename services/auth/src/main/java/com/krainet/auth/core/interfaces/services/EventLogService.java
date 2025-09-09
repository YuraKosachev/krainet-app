package com.krainet.auth.core.interfaces.services;

import com.krainet.auth.core.interfaces.services.base.Creatable;
import com.krainet.auth.core.models.dtos.AccountCreateDto;
import com.krainet.auth.core.models.entities.Account;
import com.krainet.auth.core.models.entities.EventLog;

public interface EventLogService {
    void create(EventLog eventLog);
}
