package org.example.support.dto.venda;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemVendaRequest {
    @NotNull
    public Long veiculoId;
    @Min(1)
    public int quantidade;
    // opcional: se null, usar precoVendaSugerido do veículo
    public BigDecimal precoUnitario;
}
