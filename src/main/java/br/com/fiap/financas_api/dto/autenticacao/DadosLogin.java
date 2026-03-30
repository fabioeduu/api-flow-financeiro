package br.com.fiap.financas_api.dto.autenticacao;

import jakarta.validation.constraints.NotBlank;

public record DadosLogin (
    @NotBlank(message = "Email é obrigatório")
     String email,
    @NotBlank(message = "Senha é obrigatória")
    String senha
){}
