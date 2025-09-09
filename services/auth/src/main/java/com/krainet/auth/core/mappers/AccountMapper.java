package com.krainet.auth.core.mappers;

import com.krainet.auth.core.models.dtos.AccountCreateDto;
import com.krainet.auth.core.models.dtos.AccountDto;
import com.krainet.auth.core.models.dtos.AccountUpdateDto;
import com.krainet.auth.core.models.entities.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class AccountMapper {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Account dtoToEntity(AccountCreateDto source) {
        return Account.builder()
                .username(source.login())
                .mail(source.email())
                .firstName(source.firstName())
                .lastName(source.lastName())
                .password(bCryptPasswordEncoder.encode(source.password().concat(source.login())))
                .build();
    }

    public void updateEntity(AccountUpdateDto source, Account target) {
        target.setFirstName(source.firstName());
        target.setLastName(source.lastName());
        target.setMail(source.email());

    }

    public AccountDto entityToDto(Account source) {
        return AccountDto.builder()
                .login(source.getUsername())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getMail())
                .id(source.getId())
                .build();
    }

}