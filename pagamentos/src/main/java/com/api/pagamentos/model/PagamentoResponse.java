package com.api.pagamentos.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PagamentoResponse {
    private Long id;
    private BigDecimal valor;
    private StatusTransacao statusTransacao;
    private FormaPagamento formaPagamento;
    private LocalDate dataHora;
}
