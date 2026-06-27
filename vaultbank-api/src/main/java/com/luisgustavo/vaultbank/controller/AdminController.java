package com.luisgustavo.vaultbank.controller;

import com.luisgustavo.vaultbank.dto.AccountResponseDTO;
import com.luisgustavo.vaultbank.dto.UserResponseDTO;
import com.luisgustavo.vaultbank.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        return ResponseEntity.ok(adminService.getAllAccounts());
    }

    @PatchMapping("/users/{userId}/toggle-status")
    public ResponseEntity<Void> toggleUserStatus(@PathVariable UUID userId) {
        adminService.toggleUserStatus(userId);
        return ResponseEntity.noContent().build();
    }
}
