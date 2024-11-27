package br.com.instivo.pagamento.controller;

import br.com.instivo.pagamento.dtos.PagamentoDto;
import br.com.instivo.pagamento.dtos.PagamentoRequest;
import br.com.instivo.pagamento.dtos.PagamentoResponse;
import br.com.instivo.pagamento.mappers.PagamentoMapper;
import br.com.instivo.pagamento.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Gerar novo pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pagamento criado", content = @Content(schema = @Schema(implementation = PagamentoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro nos parâmetros", content = @Content(schema = @Schema(implementation = PagamentoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro inesperado", content = @Content(schema = @Schema(implementation = PagamentoResponse.class)))
    })
    public ResponseEntity<PagamentoResponse> gerarPagamento(@Valid @RequestBody PagamentoRequest pagamentoRequest) {

        PagamentoDto dto = PagamentoMapper.toPagamentoDto(pagamentoRequest);
        PagamentoResponse response = pagamentoService.gerandoPagamento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
