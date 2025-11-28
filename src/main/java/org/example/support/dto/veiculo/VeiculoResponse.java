package org.example.support.dto.veiculo;

import org.example.support.domain.entity.Veiculo;
import org.example.support.domain.enums.Cambio;
import org.example.support.domain.enums.StatusVeiculo;
import org.example.support.domain.enums.TipoCombustivel;

import java.math.BigDecimal;

public class VeiculoResponse {
    public Long id;
    public String marca;
    public String modelo;
    public int ano;
    public String cor;
    public String placa;
    public String renavam;
    public long quilometragem;
    public TipoCombustivel tipoCombustivel;
    public Cambio cambio;
    public BigDecimal precoCompra;
    public BigDecimal precoVendaSugerido;
    public int quantidadeEmEstoque;
    public StatusVeiculo status;
    public boolean ativo;

    public static VeiculoResponse from(Veiculo v) {
        VeiculoResponse r = new VeiculoResponse();
        r.id = v.getId();
        r.marca = v.getMarca();
        r.modelo = v.getModelo();
        r.ano = v.getAno();
        r.cor = v.getCor();
        r.placa = v.getPlaca();
        r.renavam = v.getRenavam();
        r.quilometragem = v.getQuilometragem();
        r.tipoCombustivel = v.getTipoCombustivel();
        r.cambio = v.getCambio();
        r.precoCompra = v.getPrecoCompra();
        r.precoVendaSugerido = v.getPrecoVendaSugerido();
        r.quantidadeEmEstoque = v.getQuantidadeEmEstoque();
        r.status = v.getStatus();
        r.ativo = v.isAtivo();
        return r;
    }
}
