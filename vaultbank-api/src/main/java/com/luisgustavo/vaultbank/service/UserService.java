package com.luisgustavo.vaultbank.service;

import com.luisgustavo.vaultbank.dto.RegisterRequestDTO;
import com.luisgustavo.vaultbank.dto.UserResponseDTO;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.enums.Role;
import com.luisgustavo.vaultbank.exception.EmailAlreadyExistsException;
import com.luisgustavo.vaultbank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AccountService accountService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @SuppressWarnings("null")
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.email())) {
            throw new EmailAlreadyExistsException("O e-mail informado já está em uso.");
        }

        User user = User.builder()
                .name(requestDTO.name())
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        accountService.createAccountForUser(user);

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.isActive()
        );
    }
}
