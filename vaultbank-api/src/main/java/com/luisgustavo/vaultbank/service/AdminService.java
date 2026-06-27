package com.luisgustavo.vaultbank.service;

import com.luisgustavo.vaultbank.dto.AccountResponseDTO;
import com.luisgustavo.vaultbank.dto.UserResponseDTO;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.repository.AccountRepository;
import com.luisgustavo.vaultbank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AdminService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponseDTO(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getCreatedAt(), u.isActive()))
                .collect(Collectors.toList());
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(a -> new AccountResponseDTO(a.getId(), a.getAgency(), a.getAccountNumber(), a.getBalance(), a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    public void toggleUserStatus(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }
}
