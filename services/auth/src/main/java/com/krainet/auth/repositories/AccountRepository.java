package com.krainet.auth.repositories;

import com.krainet.auth.core.enums.Role;
import com.krainet.auth.core.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUsername(String username);

    @Query("select a from Account a where a.role = :role")
    List<Account> getAllByRole(Role role);
}