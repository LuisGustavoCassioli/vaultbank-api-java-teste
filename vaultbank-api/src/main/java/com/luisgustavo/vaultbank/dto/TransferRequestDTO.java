package com.luisgustavo.vaultbank.dto;

import com.luisgustavo.vaultbank.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotBlank(message = "A conta de destino é obrigatória")
        String destinationAccountNumber,

        @NotNull(message = "O valor da transferência é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor da transferência deve ser maior que zero")
        BigDecimal amount,

        @NotNull(message = "O tipo de transação (PIX ou TED) é obrigatório")
        TransactionType type
) {}
