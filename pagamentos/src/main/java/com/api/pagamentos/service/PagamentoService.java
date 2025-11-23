package com.api.pagamentos.service;

import com.api.pagamentos.model.EstornoResponse;
import com.api.pagamentos.model.PagamentoRequest;
import com.api.pagamentos.model.PagamentoResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {
    public PagamentoResponse criarPagamento(PagamentoRequest pagamentoRequest) {
        return null;
    }

    public EstornoResponse estornarPagamento(Long id) {
        return null;
    }

    public @Nullable List<PagamentoResponse> findAll() {
        return null;
    }

    public PagamentoResponse findById(Long id) {
        return null;
    }
}
