package br.com.fiap.financas_api.dto.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @Email(message = "Email inválido")
        @NotBlank(message = "Email é obrigatório")
        String email,
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String senha
){}
