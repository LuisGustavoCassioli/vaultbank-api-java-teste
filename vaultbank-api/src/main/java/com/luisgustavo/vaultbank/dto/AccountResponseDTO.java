package com.luisgustavo.vaultbank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        String agency,
        String accountNumber,
        BigDecimal balance,
        LocalDateTime createdAt
) {}
