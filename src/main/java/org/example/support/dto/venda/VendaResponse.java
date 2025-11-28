package org.example.support.dto.venda;

import org.example.support.domain.entity.Venda;
import org.example.support.domain.enums.FormaPagamento;
import org.example.support.domain.enums.VendaStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VendaResponse {
    public Long id;
    public Long clienteId;
    public String clienteNome;
    public LocalDateTime dataVenda;
    public FormaPagamento formaPagamento;
    public VendaStatus status;
    public BigDecimal valorTotal;
    public List<ItemVendaResponse> itens;

    public static VendaResponse from(Venda v) {
        VendaResponse r = new VendaResponse();
        r.id = v.getId();
        r.clienteId = v.getCliente().getId();
        r.clienteNome = v.getCliente().getNome();
        r.dataVenda = v.getDataVenda();
        r.formaPagamento = v.getFormaPagamento();
        r.status = v.getStatus();
        r.valorTotal = v.getValorTotal();
        r.itens = v.getItens().stream().map(ItemVendaResponse::from).toList();
        return r;
    }
}
