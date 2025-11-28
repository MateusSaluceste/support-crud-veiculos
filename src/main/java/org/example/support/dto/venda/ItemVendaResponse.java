package org.example.support.dto.venda;

import org.example.support.domain.entity.ItemVenda;

import java.math.BigDecimal;

public class ItemVendaResponse {
    public Long id;
    public Long veiculoId;
    public String veiculoPlaca;
    public int quantidade;
    public BigDecimal precoUnitarioNoMomento;
    public BigDecimal subtotal;

    public static ItemVendaResponse from(ItemVenda i) {
        ItemVendaResponse r = new ItemVendaResponse();
        r.id = i.getId();
        r.veiculoId = i.getVeiculo().getId();
        r.veiculoPlaca = i.getVeiculo().getPlaca();
        r.quantidade = i.getQuantidade();
        r.precoUnitarioNoMomento = i.getPrecoUnitarioNoMomento();
        r.subtotal = i.getSubtotal();
        return r;
    }
}
