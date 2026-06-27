package com.luisgustavo.vaultbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luisgustavo.vaultbank.dto.LoginRequestDTO;
import com.luisgustavo.vaultbank.dto.RegisterRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("deve_retornar_201_ao_registrar_novo_usuario")
    void deve_retornar_201_ao_registrar_novo_usuario() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
                "Teste Usuario",
                "novo_teste@email.com",
                "senhaForte123"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Teste Usuario"))
                .andExpect(jsonPath("$.email").value("novo_teste@email.com"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("deve_retornar_200_com_token_no_login_valido")
    void deve_retornar_200_com_token_no_login_valido() throws Exception {
        // 1. Cria usuário primeiro
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
                "Teste Login",
                "login_teste@email.com",
                "senha123"
        );
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // 2. Faz o login com as mesmas credenciais
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                "login_teste@email.com",
                "senha123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    @DisplayName("deve_retornar_401_no_login_invalido")
    void deve_retornar_401_no_login_invalido() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                "naoexiste@email.com",
                "senhaerrada"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden()); // O Spring Security retorna 403 para bad credentials
    }
}
