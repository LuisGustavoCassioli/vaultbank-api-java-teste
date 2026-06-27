package com.luisgustavo.vaultbank.controller;

import com.luisgustavo.vaultbank.dto.LoginRequestDTO;
import com.luisgustavo.vaultbank.dto.LoginResponseDTO;
import com.luisgustavo.vaultbank.dto.RegisterRequestDTO;
import com.luisgustavo.vaultbank.dto.UserResponseDTO;
import com.luisgustavo.vaultbank.entity.User;
import com.luisgustavo.vaultbank.security.TokenService;
import com.luisgustavo.vaultbank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = 
                new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        
        String token = tokenService.generateToken((User) auth.getPrincipal());
        
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        UserResponseDTO response = userService.register(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
