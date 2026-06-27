package com.luisgustavo.vaultbank.service;

import com.luisgustavo.vaultbank.dto.AccountResponseDTO;
import com.luisgustavo.vaultbank.entity.Account;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @SuppressWarnings("null")
    @Transactional(propagation = Propagation.MANDATORY)
    public Account createAccountForUser(User user) {
        String accountNumber;
        do {
            accountNumber = String.format("%08d", random.nextInt(100000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .user(user)
                .agency("0001")
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .build();

        return accountRepository.save(account);
    }

    public AccountResponseDTO getMyAccount(User user) {
        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada para o usuário"));

        return new AccountResponseDTO(
                account.getId(),
                account.getAgency(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }
}
