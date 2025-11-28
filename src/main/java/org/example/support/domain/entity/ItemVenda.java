package org.example.support.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.support.domain.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "item_venda")
public class ItemVenda extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Venda venda;

    @ManyToOne(optional = false)
    private Veiculo veiculo;

    @Min(1)
    private int quantidade;

    @NotNull
    private BigDecimal precoUnitarioNoMomento;

    @NotNull
    private BigDecimal subtotal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitarioNoMomento() { return precoUnitarioNoMomento; }
    public void setPrecoUnitarioNoMomento(BigDecimal precoUnitarioNoMomento) { this.precoUnitarioNoMomento = precoUnitarioNoMomento; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
