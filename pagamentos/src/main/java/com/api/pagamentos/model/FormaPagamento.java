package com.api.pagamentos.model;

import lombok.Data;

@Data
public class FormaPagamento {
    private TipoPagamento tipo;
    private Integer parcelas;
}
