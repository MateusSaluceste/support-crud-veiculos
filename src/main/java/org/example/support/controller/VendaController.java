package org.example.support.controller;

import jakarta.validation.Valid;
import org.example.support.domain.entity.Venda;
import org.example.support.domain.enums.VendaStatus;
import org.example.support.dto.venda.VendaEntrada;
import org.example.support.dto.venda.VendaSaida;
import org.example.support.repository.VendaRepository;
import org.example.support.service.VendaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    private final VendaService vendaService;
    private final VendaRepository vendaRepository;

    public VendaController(VendaService vendaService, VendaRepository vendaRepository) {
        this.vendaService = vendaService;
        this.vendaRepository = vendaRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public Page<VendaSaida> listar(@RequestParam(required = false) VendaStatus status, Pageable pageable) {
        return vendaRepository.search(status, pageable).map(VendaSaida::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaSaida obter(@PathVariable Long id) {
        return VendaSaida.from(vendaService.obterPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaSaida criar(@Valid @RequestBody VendaEntrada req) {
        Venda v = vendaService.registrarVenda(req);
        return VendaSaida.from(v);
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaSaida pagar(@PathVariable Long id) {
        return VendaSaida.from(vendaService.pagar(id));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaSaida cancelar(@PathVariable Long id) {
        return VendaSaida.from(vendaService.cancelar(id));
    }
}
