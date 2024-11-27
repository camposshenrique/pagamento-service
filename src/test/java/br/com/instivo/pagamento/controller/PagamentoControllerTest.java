package br.com.instivo.pagamento.controller;

import br.com.instivo.pagamento.dtos.PagamentoRequest;
import br.com.instivo.pagamento.dtos.PagamentoResponse;
import br.com.instivo.pagamento.dtos.TipoPagamento;
import br.com.instivo.pagamento.entity.Pagamento;
import br.com.instivo.pagamento.repository.PagamentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PagamentoControllerTest {

    private final String GERAR_PAGAMENTOS = "/pagamentos";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void pagamentoPixLimiteTest() throws Exception {
        TipoPagamento tipo = TipoPagamento.builder()
                .tipo("pix")
                .bandeira("")
                .build();

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testeLimitePix")
                .tipo(tipo)
                .valor(BigDecimal.valueOf(1001))
                .build();
        mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Pagamento não autorizado, valor superior a R$ 1000,00 para pagamento via PIX")));
    }

    @Test
    public void pagamentoSemTipoTest() throws Exception {

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testeSemTipo")
                .valor(BigDecimal.valueOf(500))
                .build();
        mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Tipo de pagamento não informado")));
    }

    @Test
    public void erroDesconhecidoTest() throws Exception {
        TipoPagamento tipo = TipoPagamento.builder()
                .tipo("pix")
                .build();

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testeCreditoSemBandeira")
                .tipo(tipo)
                .valor(BigDecimal.valueOf(500))
                .build();
        mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Erro desconhecido, favor entrar em contato com o suporte")));
    }

    @Test
    public void pagamentoPixTest() throws Exception {
        TipoPagamento tipo = TipoPagamento.builder()
                .tipo("pix")
                .bandeira("")
                .build();

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testaPix")
                .tipo(tipo)
                .valor(BigDecimal.valueOf(500))
                .build();

        String result = mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", equalTo("testaPix")))
                .andExpect(jsonPath("$.tipo", equalTo("PIX")))
                .andExpect(jsonPath("$.valor", equalTo(500)))
                .andExpect(jsonPath("$.taxa", equalTo(0)))
                .andExpect(jsonPath("$.valorLiquido", equalTo(500)))
                .andReturn().getResponse().getContentAsString();

        PagamentoResponse response = objectMapper.readValue(result, PagamentoResponse.class);
        Optional<Pagamento> pagamento = pagamentoRepository.findById(response.getId());
        Assertions.assertTrue(pagamento.isPresent());
        pagamentoRepository.delete(pagamento.get());
    }

    @Test
    public void pagamentoDebitoTest() throws Exception {
        TipoPagamento tipo = TipoPagamento.builder()
                .tipo("debito")
                .bandeira("")
                .build();

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testaDebito")
                .tipo(tipo)
                .valor(BigDecimal.valueOf(5000))
                .build();

        String result = mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", equalTo("testaDebito")))
                .andExpect(jsonPath("$.tipo", equalTo("DEBITO")))
                .andExpect(jsonPath("$.valor", equalTo(5000)))
                .andExpect(jsonPath("$.taxa", equalTo(2.5)))
                .andExpect(jsonPath("$.valorLiquido", equalTo(5125.0)))
                .andReturn().getResponse().getContentAsString();

        PagamentoResponse response = objectMapper.readValue(result, PagamentoResponse.class);
        Optional<Pagamento> pagamento = pagamentoRepository.findById(response.getId());
        Assertions.assertTrue(pagamento.isPresent());
        pagamentoRepository.delete(pagamento.get());
    }

    @Test
    public void pagamentoCreditoVisaTest() throws Exception {
        TipoPagamento tipo = TipoPagamento.builder()
                .tipo("credito")
                .bandeira("visa")
                .build();

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testaCreditoVisa")
                .tipo(tipo)
                .valor(BigDecimal.valueOf(5000))
                .build();

        String result = mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", equalTo("testaCreditoVisa")))
                .andExpect(jsonPath("$.tipo", equalTo("CREDITO")))
                .andExpect(jsonPath("$.valor", equalTo(5000)))
                .andExpect(jsonPath("$.taxa", equalTo(5.0)))
                .andExpect(jsonPath("$.valorLiquido", equalTo(5250.0)))
                .andReturn().getResponse().getContentAsString();

        PagamentoResponse response = objectMapper.readValue(result, PagamentoResponse.class);
        Optional<Pagamento> pagamento = pagamentoRepository.findById(response.getId());
        Assertions.assertTrue(pagamento.isPresent());
        pagamentoRepository.delete(pagamento.get());
    }

    @Test
    public void pagamentoCreditoMasterTest() throws Exception {
        TipoPagamento tipo = TipoPagamento.builder()
                .tipo("credito")
                .bandeira("mastercard")
                .build();

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testaCreditoMaster")
                .tipo(tipo)
                .valor(BigDecimal.valueOf(5000))
                .build();

        String result = mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", equalTo("testaCreditoMaster")))
                .andExpect(jsonPath("$.tipo", equalTo("CREDITO")))
                .andExpect(jsonPath("$.valor", equalTo(5000)))
                .andExpect(jsonPath("$.taxa", equalTo(4.0)))
                .andExpect(jsonPath("$.valorLiquido", equalTo(5200.0)))
                .andReturn().getResponse().getContentAsString();

        PagamentoResponse response = objectMapper.readValue(result, PagamentoResponse.class);
        Optional<Pagamento> pagamento = pagamentoRepository.findById(response.getId());
        Assertions.assertTrue(pagamento.isPresent());
        pagamentoRepository.delete(pagamento.get());
    }

    @Test
    public void pagamentoCreditoEloTest() throws Exception {
        TipoPagamento tipo = TipoPagamento.builder()
                .tipo("credito")
                .bandeira("elo")
                .build();

        PagamentoRequest request = PagamentoRequest.builder()
                .nome("testaCreditoElo")
                .tipo(tipo)
                .valor(BigDecimal.valueOf(5000))
                .build();

        String result = mockMvc.perform(post(GERAR_PAGAMENTOS)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", equalTo("testaCreditoElo")))
                .andExpect(jsonPath("$.tipo", equalTo("CREDITO")))
                .andExpect(jsonPath("$.valor", equalTo(5000)))
                .andExpect(jsonPath("$.taxa", equalTo(3.0)))
                .andExpect(jsonPath("$.valorLiquido", equalTo(5150.0)))
                .andReturn().getResponse().getContentAsString();

        PagamentoResponse response = objectMapper.readValue(result, PagamentoResponse.class);
        Optional<Pagamento> pagamento = pagamentoRepository.findById(response.getId());
        Assertions.assertTrue(pagamento.isPresent());
        pagamentoRepository.delete(pagamento.get());
    }

}
