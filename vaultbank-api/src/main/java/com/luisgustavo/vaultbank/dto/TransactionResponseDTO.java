package com.luisgustavo.vaultbank.dto;

import com.luisgustavo.vaultbank.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        String sourceAccountNumber,
        String destinationAccountNumber,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime createdAt
) {}
