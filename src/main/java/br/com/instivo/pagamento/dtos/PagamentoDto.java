package br.com.instivo.pagamento.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDto {

    private String nome;

    private TipoPagamento tipo;

    private BigDecimal valor;

}
