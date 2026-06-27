package com.luisgustavo.vaultbank.service;

import com.luisgustavo.vaultbank.dto.RegisterRequestDTO;
import com.luisgustavo.vaultbank.dto.UserResponseDTO;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.entity.Account;
import com.luisgustavo.vaultbank.enums.Role;
import com.luisgustavo.vaultbank.exception.EmailAlreadyExistsException;
import com.luisgustavo.vaultbank.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("deve_cadastrar_usuario_com_sucesso")
    void deve_cadastrar_usuario_com_sucesso() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Luis", "luis@teste.com", "senha123");
        
        User savedUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password("encoded_password")
                .role(Role.USER)
                .build();
        savedUser.setId(UUID.randomUUID());
        savedUser.setActive(true);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(accountService.createAccountForUser(any(User.class))).thenReturn(new Account());

        // Act
        UserResponseDTO response = userService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(savedUser.getId(), response.id());
        assertEquals("Luis", response.name());
        assertEquals("luis@teste.com", response.email());
        assertEquals(Role.USER, response.role());
        assertTrue(response.active());

        verify(userRepository, times(1)).existsByEmail(request.email());
        verify(passwordEncoder, times(1)).encode(request.password());
        verify(userRepository, times(1)).save(any(User.class));
        verify(accountService, times(1)).createAccountForUser(any(User.class));
    }

    @Test
    @DisplayName("deve_retornar_erro_quando_email_ja_existir")
    void deve_retornar_erro_quando_email_ja_existir() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Luis", "luis@teste.com", "senha123");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class, 
                () -> userService.register(request)
        );

        assertEquals("O e-mail informado já está em uso.", exception.getMessage());
        
        verify(userRepository, times(1)).existsByEmail(request.email());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(accountService, never()).createAccountForUser(any(User.class));
    }
}
