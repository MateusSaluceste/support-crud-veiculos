package org.example.support.dto.venda;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.support.domain.enums.FormaPagamento;

import java.util.List;

public class VendaCreateRequest {
    @NotNull
    public Long clienteId;

    @NotNull
    public FormaPagamento formaPagamento;

    @Valid
    @NotEmpty
    public List<ItemVendaRequest> itens;
}
