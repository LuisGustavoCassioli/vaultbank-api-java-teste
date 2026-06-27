package com.luisgustavo.vaultbank.dto;

import java.math.BigDecimal;
import java.util.List;

public record StatementResponseDTO(
        String accountNumber,
        BigDecimal currentBalance,
        BigDecimal totalIncoming,
        BigDecimal totalOutgoing,
        List<TransactionResponseDTO> transactions
) {
}
