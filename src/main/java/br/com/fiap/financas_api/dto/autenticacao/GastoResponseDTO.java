package br.com.fiap.financas_api.dto.autenticacao;

import br.com.fiap.financas_api.model.Gasto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


public record GastoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        String moeda,
        BigDecimal valorConvertido,
        LocalDate data,
        String categoria
) {
    public GastoResponseDTO(Gasto gasto) {
        this(
                gasto.getId(),
                gasto.getDescricao(),
                gasto.getValor(),
                gasto.getMoeda(),
                gasto.getValorConvertido(),
                gasto.getData(),
                gasto.getCategoria()
        );
    }
}

