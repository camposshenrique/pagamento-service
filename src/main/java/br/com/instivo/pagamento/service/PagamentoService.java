package br.com.instivo.pagamento.service;

import br.com.instivo.pagamento.dtos.PagamentoDto;
import br.com.instivo.pagamento.dtos.PagamentoResponse;
import br.com.instivo.pagamento.entity.Pagamento;
import br.com.instivo.pagamento.exceptions.ValorPixException;
import br.com.instivo.pagamento.mappers.PagamentoMapper;
import br.com.instivo.pagamento.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PagamentoService {

    private static final BigDecimal TAXA_VISA = BigDecimal.valueOf(0.05);
    private static final BigDecimal TAXA_MASTERCARD = BigDecimal.valueOf(0.04);
    private static final BigDecimal TAXA_ELO = BigDecimal.valueOf(0.03);
    private static final BigDecimal TAXA_DEBITO = BigDecimal.valueOf(0.025);
    private static final BigDecimal TAXA_PIX = BigDecimal.ZERO;
    private static  final BigDecimal LIMITE_PIX = BigDecimal.valueOf(1000);
    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    @Autowired
    public PagamentoRepository pagamentoRepository;

    public PagamentoResponse gerandoPagamento(PagamentoDto pagamentoDto){
        switch (pagamentoDto.getTipo().getTipo().toUpperCase()) {
            case "CREDITO" -> {
                return PagamentoMapper.toResponse(calcularPagamentoCredito(pagamentoDto));
            }
            case "DEBITO" -> {
                return PagamentoMapper.toResponse(calcularPagamentoDebito(pagamentoDto));
            }
            case "PIX" -> {
                return PagamentoMapper.toResponse(calcularPagamentoPix(pagamentoDto));
            }
        }
        return null;
    }

    public Pagamento calcularPagamentoCredito(PagamentoDto pagamentoDto){
        switch (pagamentoDto.getTipo().getBandeira()) {
            case "VISA" -> {
                return pagamentoRepository.save(criarPagamento(pagamentoDto, TAXA_VISA));
            }
            case "MASTERCARD" -> {
                return pagamentoRepository.save(criarPagamento(pagamentoDto, TAXA_MASTERCARD));
            }
            case "ELO" -> {
                return pagamentoRepository.save(criarPagamento(pagamentoDto, TAXA_ELO));
            }
        }
        return null;
    }


    public Pagamento calcularPagamentoDebito(PagamentoDto pagamentoDto){
        return pagamentoRepository.save(criarPagamento(pagamentoDto, TAXA_DEBITO));
    }

    public Pagamento calcularPagamentoPix(PagamentoDto pagamentoDto){

        if((pagamentoDto.getValor().compareTo(LIMITE_PIX)) > 0){
            throw new ValorPixException();
        }
        return pagamentoRepository.save(criarPagamento(pagamentoDto, TAXA_PIX));
    }


    public Pagamento criarPagamento(PagamentoDto pagamentoDto, BigDecimal taxa){

        BigDecimal valorLiquido = pagamentoDto.getValor().add(pagamentoDto.getValor().multiply(taxa));
        return Pagamento.builder()
                .id(UUID.randomUUID())
                .valor(pagamentoDto.getValor())
                .nome(pagamentoDto.getNome())
                .taxa(taxa.multiply(CEM))
                .valorLiquido(valorLiquido)
                .tipo(pagamentoDto.getTipo().getTipo())
                .build();

    }

}
