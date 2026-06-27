package com.luisgustavo.vaultbank.dto;

import com.luisgustavo.vaultbank.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt,
        boolean active
) {}
