package br.com.instivo.pagamento.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoResponse {

    private UUID id;
    private String nome;
    private String tipo;
    private BigDecimal valor;
    private BigDecimal taxa;
    private BigDecimal valorLiquido;

}
