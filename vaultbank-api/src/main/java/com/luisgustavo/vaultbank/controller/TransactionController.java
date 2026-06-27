package com.luisgustavo.vaultbank.controller;

import com.luisgustavo.vaultbank.dto.TransactionResponseDTO;
import com.luisgustavo.vaultbank.dto.TransferRequestDTO;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransferRequestDTO requestDTO) {
        
        TransactionResponseDTO response = transactionService.transfer(user, requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponseDTO>> getHistory(@AuthenticationPrincipal User user) {
        List<TransactionResponseDTO> history = transactionService.getHistory(user);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/statement")
    public ResponseEntity<com.luisgustavo.vaultbank.dto.StatementResponseDTO> getStatement(
            @AuthenticationPrincipal User user,
            @org.springframework.web.bind.annotation.RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) com.luisgustavo.vaultbank.enums.TransactionType type) {
        
        com.luisgustavo.vaultbank.dto.StatementResponseDTO statement = transactionService.getStatement(user, startDate, endDate, type);
        return ResponseEntity.ok(statement);
    }
}
