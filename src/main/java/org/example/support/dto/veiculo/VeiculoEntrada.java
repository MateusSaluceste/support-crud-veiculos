package org.example.support.dto.veiculo;

import jakarta.validation.constraints.*;
import org.example.support.domain.enums.Cambio;
import org.example.support.domain.enums.StatusVeiculo;
import org.example.support.domain.enums.TipoCombustivel;

import java.math.BigDecimal;

public class VeiculoEntrada {
    @NotBlank
    public String marca;
    @NotBlank
    public String modelo;
    @Min(1900)
    public int ano;
    public String cor;
    @NotBlank
    @Size(min=5, max=10)
    public String placa;
    public String renavam;
    @Min(0)
    public long quilometragem;
    public TipoCombustivel tipoCombustivel;
    public Cambio cambio;
    public BigDecimal precoCompra;
    @NotNull
    @DecimalMin("0.0")
    public BigDecimal precoVendaSugerido;
    @Min(0)
    public int quantidadeEmEstoque;
    public StatusVeiculo status = StatusVeiculo.DISPONIVEL;
}
