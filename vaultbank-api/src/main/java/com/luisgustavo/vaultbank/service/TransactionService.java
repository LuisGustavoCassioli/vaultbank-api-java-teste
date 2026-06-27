package com.luisgustavo.vaultbank.service;

import com.luisgustavo.vaultbank.dto.TransactionResponseDTO;
import com.luisgustavo.vaultbank.dto.TransferRequestDTO;
import com.luisgustavo.vaultbank.entity.Account;
import com.luisgustavo.vaultbank.entity.Transaction;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.exception.AccountNotFoundException;
import com.luisgustavo.vaultbank.exception.InsufficientBalanceException;
import com.luisgustavo.vaultbank.repository.AccountRepository;
import com.luisgustavo.vaultbank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @SuppressWarnings("null")
    @Transactional
    public TransactionResponseDTO transfer(User loggedUser, TransferRequestDTO requestDTO) {
        Account source = accountRepository.findByUserId(loggedUser.getId())
                .orElseThrow(() -> new AccountNotFoundException("Conta de origem não encontrada."));

        Account destination = accountRepository.findByAccountNumber(requestDTO.destinationAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Conta de destino não encontrada."));

        if (source.getId().equals(destination.getId())) {
            throw new IllegalArgumentException("Não é possível transferir para a própria conta.");
        }

        if (source.getBalance().compareTo(requestDTO.amount()) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para realizar a transferência.");
        }

        source.setBalance(source.getBalance().subtract(requestDTO.amount()));
        destination.setBalance(destination.getBalance().add(requestDTO.amount()));

        accountRepository.save(source);
        accountRepository.save(destination);

        Transaction transaction = Transaction.builder()
                .sourceAccount(source)
                .destinationAccount(destination)
                .amount(requestDTO.amount())
                .type(requestDTO.type())
                .build();

        transaction = transactionRepository.save(transaction);

        return mapToDTO(transaction);
    }

    public List<TransactionResponseDTO> getHistory(User loggedUser) {
        Account account = accountRepository.findByUserId(loggedUser.getId())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        return transactionRepository.findHistoryByAccountId(account.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public com.luisgustavo.vaultbank.dto.StatementResponseDTO getStatement(
            User loggedUser,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate,
            com.luisgustavo.vaultbank.enums.TransactionType type
    ) {
        Account account = accountRepository.findByUserId(loggedUser.getId())
                .orElseThrow(() -> new AccountNotFoundException("Conta não encontrada."));

        java.time.LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : java.time.LocalDateTime.now().minusDays(30);
        java.time.LocalDateTime end = (endDate != null) ? endDate.atTime(23, 59, 59) : java.time.LocalDateTime.now();

        List<Transaction> transactions;
        if (type == null) {
            transactions = transactionRepository.findStatementByDates(account.getId(), start, end);
        } else {
            transactions = transactionRepository.findStatementByDatesAndType(account.getId(), start, end, type);
        }

        java.math.BigDecimal totalIncoming = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalOutgoing = java.math.BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if (t.getDestinationAccount().getId().equals(account.getId())) {
                totalIncoming = totalIncoming.add(t.getAmount());
            } else if (t.getSourceAccount().getId().equals(account.getId())) {
                totalOutgoing = totalOutgoing.add(t.getAmount());
            }
        }

        List<TransactionResponseDTO> dtos = transactions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new com.luisgustavo.vaultbank.dto.StatementResponseDTO(
                account.getAccountNumber(),
                account.getBalance(),
                totalIncoming,
                totalOutgoing,
                dtos
        );
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSourceAccount().getAccountNumber(),
                transaction.getDestinationAccount().getAccountNumber(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt()
        );
    }
}
