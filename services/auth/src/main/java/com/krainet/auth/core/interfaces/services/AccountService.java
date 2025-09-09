package com.krainet.auth.core.interfaces.services;

import com.krainet.auth.core.interfaces.services.base.*;
import com.krainet.auth.core.models.dtos.*;
import com.krainet.auth.core.models.entities.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService,
        Creatable<AccountCreateDto, Account>,
        Updateable<AccountUpdateDto, Account>,
        Pagetable<Account>,
        Deletable,
        Searchable<Account>
{
    AccountDto getAccountBy(AuthorizationData data);
    void updateRole(RoleUpdateDto roleUpdateDto);
}
