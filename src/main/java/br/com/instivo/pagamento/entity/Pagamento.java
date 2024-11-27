package br.com.instivo.pagamento.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_pagamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagamento {
    @Id
    private UUID id;
    private String nome;
    private String tipo;
    private BigDecimal valor;
    private BigDecimal taxa;
    private BigDecimal valorLiquido;

}
