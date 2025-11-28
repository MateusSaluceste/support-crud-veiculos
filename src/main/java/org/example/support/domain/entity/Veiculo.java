package org.example.support.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.example.support.domain.BaseEntity;
import org.example.support.domain.enums.Cambio;
import org.example.support.domain.enums.StatusVeiculo;
import org.example.support.domain.enums.TipoCombustivel;

import java.math.BigDecimal;

@Entity
@Table(name = "veiculo", uniqueConstraints = {
        @UniqueConstraint(name = "uk_veiculo_placa", columnNames = {"placa"})
})
public class Veiculo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @Min(1900)
    private int ano;

    private String cor;

    @NotBlank
    @Size(min = 5, max = 10)
    private String placa;

    private String renavam;

    @Min(0)
    private long quilometragem;

    @Enumerated(EnumType.STRING)
    private TipoCombustivel tipoCombustivel;

    @Enumerated(EnumType.STRING)
    private Cambio cambio;

    private BigDecimal precoCompra;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal precoVendaSugerido;

    @Column(name = "quantidade_estoque", nullable = false)
    @Min(0)
    private int quantidadeEmEstoque;

    @Enumerated(EnumType.STRING)
    private StatusVeiculo status = StatusVeiculo.DISPONIVEL;

    private boolean ativo = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getRenavam() { return renavam; }
    public void setRenavam(String renavam) { this.renavam = renavam; }
    public long getQuilometragem() { return quilometragem; }
    public void setQuilometragem(long quilometragem) { this.quilometragem = quilometragem; }
    public TipoCombustivel getTipoCombustivel() { return tipoCombustivel; }
    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) { this.tipoCombustivel = tipoCombustivel; }
    public Cambio getCambio() { return cambio; }
    public void setCambio(Cambio cambio) { this.cambio = cambio; }
    public BigDecimal getPrecoCompra() { return precoCompra; }
    public void setPrecoCompra(BigDecimal precoCompra) { this.precoCompra = precoCompra; }
    public BigDecimal getPrecoVendaSugerido() { return precoVendaSugerido; }
    public void setPrecoVendaSugerido(BigDecimal precoVendaSugerido) { this.precoVendaSugerido = precoVendaSugerido; }
    public int getQuantidadeEmEstoque() { return quantidadeEmEstoque; }
    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) { this.quantidadeEmEstoque = quantidadeEmEstoque; }
    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
