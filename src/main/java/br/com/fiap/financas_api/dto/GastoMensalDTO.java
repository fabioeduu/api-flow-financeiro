package br.com.fiap.financas_api.dto;

import java.math.BigDecimal;

public record GastoMensalDTO(
        Object mesAno,
        BigDecimal total
) {

    public GastoMensalDTO(Object mesAno, BigDecimal total) {
        this.mesAno = mesAno != null ? mesAno.toString() : "";
        this.total = total != null ? total : BigDecimal.ZERO;
    }

    public String getMesAno() {
        return mesAno.toString();
    }
}

