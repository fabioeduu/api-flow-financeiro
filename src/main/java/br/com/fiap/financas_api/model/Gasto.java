package br.com.fiap.financas_api.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "tb_gasto")
@Data
@Entity
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String descricao;
    private String categoria;
    @Column(precision = 19, scale = 2)
    private BigDecimal valor;
    private String moeda;
    @Column(precision = 19, scale = 4)
    private BigDecimal valorConvertido;
    private LocalDate data;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
