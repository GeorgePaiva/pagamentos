package com.api.pagamentos.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoRequest {
    private BigDecimal valor;
    private String numeroCartao;
    private String titularCartao;
    private String validade; // mm/aa
    private String cvv;
    private TipoPagamento tipoPagamento;
    private Integer parcelas;
}
