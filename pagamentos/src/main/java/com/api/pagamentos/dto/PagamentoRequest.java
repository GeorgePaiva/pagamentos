package com.api.pagamentos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoRequest {

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal valor;
    @NotBlank
    @Pattern(regexp = "\\d{12,19}", message = "card number must be digits (12-19)")
    private String cartao;
    @NotNull
    private FormaPagamentoDTO formaPagamento;
}
