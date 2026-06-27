package com.luisgustavo.vaultbank.repository;

import com.luisgustavo.vaultbank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByAccountNumber(String accountNumber);
    Optional<Account> findByUserId(UUID userId);
    Optional<Account> findByAccountNumber(String accountNumber);
}
