package com.luisgustavo.vaultbank.controller;

import com.luisgustavo.vaultbank.dto.AccountResponseDTO;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponseDTO> getMyAccount(@AuthenticationPrincipal User user) {
        AccountResponseDTO accountInfo = accountService.getMyAccount(user);
        return ResponseEntity.ok(accountInfo);
    }
}
