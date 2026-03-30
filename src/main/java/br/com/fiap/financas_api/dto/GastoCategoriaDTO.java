package br.com.fiap.financas_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

public record GastoCategoriaDTO(
     String categoria,
     BigDecimal total
){}

