package com.api.pagamentos.service;

import com.api.pagamentos.dto.*;
import com.api.pagamentos.entity.DescricaoEmbeddable;
import com.api.pagamentos.entity.FormaPagamentoEmbeddable;
import com.api.pagamentos.entity.PagamentoEntity;
import com.api.pagamentos.entity.enums.StatusTransacao;
import com.api.pagamentos.exception.ResourceNotFoundException;
import com.api.pagamentos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Transactional
    public PagamentoResponse createPayment(PagamentoRequest request) {
        validateRequest(request);

        String id = UUID.randomUUID().toString();
        String cartaoMask = maskCard(request.getCartao());

        // determinar status: regra de exemplo (último dígito do cartão par => AUTORIZADO)
        String status = determineStatus(request.getCartao());

        // montar descricao
        DescricaoEmbeddable desc = new DescricaoEmbeddable();
        desc.setValor(request.getValor().setScale(2).toString());
        desc.setDataHora(OffsetDateTime.now().format(dtf));
        desc.setEstabelecimento("PetShop Mundo cão"); // ou dynamic se necessário
        desc.setNsu(generateNsu());
        desc.setCodigoAutorizacao(generateCodigoAutorizacao());
        desc.setStatus(status);

        // forma pagamento
        FormaPagamentoEmbeddable forma = new FormaPagamentoEmbeddable();
        forma.setTipo(request.getFormaPagamento().getTipo());
        forma.setParcelas(request.getFormaPagamento().getParcelas());

        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(id);
        entity.setCartao(cartaoMask);
        entity.setDescricao(desc);
        entity.setFormaPagamento(forma);
        entity.setDataHora(OffsetDateTime.now());

        repository.save(entity);

        return toResponse(entity);
    }

    @Transactional
    public PagamentoResponse refund(String id) {
        PagamentoEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));

        // se já cancelado, só retorna o mesmo objeto com status CANCELADO
        if ("CANCELADO".equalsIgnoreCase(entity.getDescricao().getStatus())) {
            return toResponse(entity);
        }

        // atualiza status
        entity.getDescricao().setStatus("CANCELADO");
        // opcional: atualizar codigoAutorizacao/nsu para estorno (aqui mantemos)
        repository.save(entity);

        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public PagamentoResponse findById(String id) {
        PagamentoEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ---------- helper methods ----------
    private void validateRequest(PagamentoRequest req) {
        if (req.getValor() == null || req.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("valor deve ser maior que zero");
        }
        if (req.getCartao() == null || !req.getCartao().matches("\\d{12,19}")) {
            throw new IllegalArgumentException("cartao inválido");
        }
        if (req.getFormaPagamento() == null) {
            throw new IllegalArgumentException("formaPagamento é obrigatório");
        }
        String tipo = req.getFormaPagamento().getTipo();
        if (!("AVISTA".equalsIgnoreCase(tipo)
                || "PARCELADO_LOJA".equalsIgnoreCase(tipo)
                || "PARCELADO_EMISSOR".equalsIgnoreCase(tipo))) {
            throw new IllegalArgumentException("tipo de pagamento inválido");
        }
    }

    private String maskCard(String card) {
        int len = card.length();
        if (len <= 8) return card; // caso raro
        String last4 = card.substring(len - 4);
        return card.substring(0, 4) + "********" + last4; // formatação similar à imagem
    }

    private String determineStatus(String card) {
        char last = card.charAt(card.length() - 1);
        if (!Character.isDigit(last)) return StatusTransacao.NEGADO.getValue();
        int d = Character.getNumericValue(last);
        return (d % 2 == 0) ? StatusTransacao.AUTORIZADO.getValue() : StatusTransacao.NEGADO.getValue();
    }

    private String generateNsu() {
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits())).replace("-", "").substring(0, 10);
    }

    private String generateCodigoAutorizacao() {
        int n = (int) (Math.random() * 900000000) + 100000000;
        return String.valueOf(n);
    }

    private PagamentoResponse toResponse(PagamentoEntity entity) {
        PagamentoResponse resp = new PagamentoResponse();

        TransacaoDTO t = new TransacaoDTO();
        t.setCartao(entity.getCartao());
        t.setId(entity.getId());

        DescricaoDTO d = new DescricaoDTO();
        DescricaoEmbeddable src = entity.getDescricao();
        d.setValor(src.getValor());
        d.setDataHora(src.getDataHora());
        d.setEstabelecimento(src.getEstabelecimento());
        d.setNsu(src.getNsu());
        d.setCodigoAutorizacao(src.getCodigoAutorizacao());
        d.setStatus(src.getStatus());

        t.setDescricao(d);
        resp.setTransacao(t);

        FormaPagamentoDTO f = new FormaPagamentoDTO();
        FormaPagamentoEmbeddable fe = entity.getFormaPagamento();
        f.setTipo(fe.getTipo());
        f.setParcelas(fe.getParcelas());
        resp.setFormaPagamento(f);

        return resp;
    }
}
