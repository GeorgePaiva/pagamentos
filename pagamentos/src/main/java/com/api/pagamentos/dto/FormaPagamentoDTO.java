package com.api.pagamentos.dto;

import com.api.pagamentos.entity.enums.TipoPagamento;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FormaPagamentoDTO {

    @NotBlank
    private String tipo;
    @NotBlank
    private String parcelas;
}
