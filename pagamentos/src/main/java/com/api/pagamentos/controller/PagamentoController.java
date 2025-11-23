package com.api.pagamentos.controller;

import com.api.pagamentos.model.EstornoResponse;
import com.api.pagamentos.model.PagamentoRequest;
import com.api.pagamentos.model.PagamentoResponse;
import com.api.pagamentos.model.StatusTransacao;
import com.api.pagamentos.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<?> criaPagamento(@RequestBody PagamentoRequest pagamentoRequest) {
        try {
            PagamentoResponse pagamentoResponse = pagamentoService.criarPagamento(pagamentoRequest);
            if (pagamentoResponse.getStatusTransacao() == StatusTransacao.AUTORIZADO) {
                return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoResponse);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoResponse);
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error",
                    "Erro ao processar pagamento"));
        }
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<?> estornaPagamento(@PathVariable Long id) {
        try {
            EstornoResponse estornoResponse = pagamentoService.estornarPagamento(id);
            return ResponseEntity.ok(estornoResponse);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error",
                    "Erro ao processar estorno"));
        }
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponse>> findAll() {
        return ResponseEntity.ok(pagamentoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            PagamentoResponse pagamentoResponse = pagamentoService.findById(id);
            return ResponseEntity.ok(pagamentoResponse);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error",
                    "Erro ao buscar transação"));
        }
    }
}
