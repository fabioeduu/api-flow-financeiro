package br.com.fiap.financas_api.dto;


import lombok.Data;

@Data
public class TokenResponseDTO {
    private String tokenAcesso;
    private String email;
}
