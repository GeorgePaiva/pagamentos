package com.api.pagamentos.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Transacao {
    private String cartao;
    private Long id;
    private Descricao descricao;
    private FormaPagamento formaPagamento;

}
