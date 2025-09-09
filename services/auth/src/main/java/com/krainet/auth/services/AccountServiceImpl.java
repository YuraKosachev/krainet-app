package com.krainet.auth.services;

import com.krainet.auth.core.enums.AccountStatus;
import com.krainet.auth.core.exceptions.BlockedStatusException;
import com.krainet.auth.core.interfaces.services.AccountService;
import com.krainet.auth.core.mappers.AccountMapper;
import com.krainet.auth.core.models.dtos.*;
import com.krainet.auth.core.models.entities.Account;
import com.krainet.auth.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public <T> T create(AccountCreateDto account, Function<Account, T> mapper) {
        var entity = accountRepository.save(accountMapper.dtoToEntity(account));
        return mapper.apply(entity);
    }

    @Override
    public <T> T update(AccountUpdateDto account, Function<Account, T> mapper) {
        var entity = accountRepository.findById(account.id())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        accountMapper.updateEntity(account, entity);
        return mapper.apply(accountRepository.save(entity));
    }

    @Override
    public <T> T findById(UUID id, Function<Account, T> mapper) {
        return accountRepository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public AccountDto getAccountBy(AuthorizationData data) {
        var account = (Account) loadUserByUsername(data.login());
        if(account.getStatus() == AccountStatus.BLOCKED) {
            throw new BlockedStatusException("Your account is blocked");
        }
        if (!bCryptPasswordEncoder.matches(data.password().concat(data.login()), account.getPassword())) {
            throw new UsernameNotFoundException("Username or password is incorrect");
        }
        return accountMapper.entityToDto(account);
    }

    @Override
    public void updateRole(RoleUpdateDto roleUpdateDto) {
        var entity = accountRepository.findById(roleUpdateDto.accountId())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        entity.setRole(roleUpdateDto.role());
        accountRepository.save(entity);
    }

    @Override
    public void deleteById(UUID id) {
        var entity = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        accountRepository.delete(entity);
    }

    @Override
    public <T> Page<T> getPages(Pageable pageable, Function<Account, T> mapper) {
        return accountRepository.
                findAll(pageable)
                .map(mapper);
    }
}