package br.com.instivo.pagamento.mappers;

import br.com.instivo.pagamento.dtos.PagamentoDto;
import br.com.instivo.pagamento.dtos.PagamentoRequest;
import br.com.instivo.pagamento.dtos.PagamentoResponse;
import br.com.instivo.pagamento.dtos.TipoPagamento;
import br.com.instivo.pagamento.entity.Pagamento;


public class PagamentoMapper {

    public static PagamentoDto toPagamentoDto(PagamentoRequest pagamentoRequest){

        TipoPagamento tipo = TipoPagamento.builder()
                .tipo(pagamentoRequest.getTipo().getTipo().toUpperCase())
                .bandeira(pagamentoRequest.getTipo().getBandeira().toUpperCase())
                .build();

        return PagamentoDto.builder()
                .nome(pagamentoRequest.getNome())
                .tipo(tipo)
                .valor(pagamentoRequest.getValor())
                .build();
    }

    public static PagamentoResponse toResponse(Pagamento pagamento){
        return PagamentoResponse.builder()
                .id(pagamento.getId())
                .nome(pagamento.getNome())
                .taxa(pagamento.getTaxa())
                .tipo(pagamento.getTipo().toUpperCase())
                .valor(pagamento.getValor())
                .valorLiquido(pagamento.getValorLiquido())
                .build();
    }

}
