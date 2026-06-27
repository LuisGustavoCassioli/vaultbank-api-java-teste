package com.luisgustavo.vaultbank.service;

import com.luisgustavo.vaultbank.dto.TransactionResponseDTO;
import com.luisgustavo.vaultbank.dto.TransferRequestDTO;
import com.luisgustavo.vaultbank.entity.Account;
import com.luisgustavo.vaultbank.entity.Transaction;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.enums.TransactionType;
import com.luisgustavo.vaultbank.exception.InsufficientBalanceException;
import com.luisgustavo.vaultbank.repository.AccountRepository;
import com.luisgustavo.vaultbank.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("deve_realizar_transferencia_com_sucesso")
    void deve_realizar_transferencia_com_sucesso() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(UUID.randomUUID());

        Account source = new Account();
        source.setId(UUID.randomUUID());
        source.setAccountNumber("12345");
        source.setBalance(new BigDecimal("1000.00"));

        Account dest = new Account();
        dest.setId(UUID.randomUUID());
        dest.setAccountNumber("54321");
        dest.setBalance(new BigDecimal("0.00"));

        TransferRequestDTO request = new TransferRequestDTO("54321", new BigDecimal("200.00"), TransactionType.PIX);

        Transaction savedTransaction = Transaction.builder()
                .sourceAccount(source)
                .destinationAccount(dest)
                .amount(request.amount())
                .type(request.type())
                .build();
        savedTransaction.setId(UUID.randomUUID());

        when(accountRepository.findByUserId(loggedUser.getId())).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber(request.destinationAccountNumber())).thenReturn(Optional.of(dest));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        TransactionResponseDTO response = transactionService.transfer(loggedUser, request);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("800.00"), source.getBalance());
        assertEquals(new BigDecimal("200.00"), dest.getBalance());
        assertEquals(request.amount(), response.amount());

        verify(accountRepository, times(1)).save(source);
        verify(accountRepository, times(1)).save(dest);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("deve_retornar_erro_por_saldo_insuficiente")
    void deve_retornar_erro_por_saldo_insuficiente() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(UUID.randomUUID());

        Account source = new Account();
        source.setId(UUID.randomUUID());
        source.setBalance(new BigDecimal("100.00"));

        Account dest = new Account();
        dest.setId(UUID.randomUUID());
        dest.setAccountNumber("54321");

        TransferRequestDTO request = new TransferRequestDTO("54321", new BigDecimal("200.00"), TransactionType.PIX);

        when(accountRepository.findByUserId(loggedUser.getId())).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber(request.destinationAccountNumber())).thenReturn(Optional.of(dest));

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class, 
                () -> transactionService.transfer(loggedUser, request)
        );

        assertEquals("Saldo insuficiente para realizar a transferência.", exception.getMessage());
        
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
