package com.api.pagamentos.model;

import lombok.Data;

@Data
public class Descricao {
    private String valor;
    private String dataHora;
    private String estabelecimento;
    private String nsu;
    private String codigoAutorizacao;
    private StatusTransacao status;
}
