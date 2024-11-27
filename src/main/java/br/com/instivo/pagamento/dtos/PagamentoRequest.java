package br.com.instivo.pagamento.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoRequest {

    private String nome;
    @NotNull(message = "Tipo de pagamento não informado")
    private TipoPagamento tipo;
    private BigDecimal valor;

}
