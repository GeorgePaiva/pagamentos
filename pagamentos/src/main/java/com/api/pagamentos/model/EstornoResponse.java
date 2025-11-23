package com.api.pagamentos.model;

import java.time.LocalDate;

public class EstornoResponse {
    private Long id;
    private boolean estornado;
    private LocalDate dataHora;
    private String message;
}
