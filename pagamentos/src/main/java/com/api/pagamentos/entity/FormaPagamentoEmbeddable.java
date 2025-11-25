package com.api.pagamentos.entity;

import com.api.pagamentos.entity.enums.TipoPagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class FormaPagamentoEmbeddable {

    @Column(name = "tipo")
    private String tipo;
    @Column(name = "parcelas")
    private String parcelas;
}
