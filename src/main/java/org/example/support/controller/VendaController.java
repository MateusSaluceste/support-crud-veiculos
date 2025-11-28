package org.example.support.controller;

import jakarta.validation.Valid;
import org.example.support.domain.entity.Venda;
import org.example.support.domain.enums.VendaStatus;
import org.example.support.dto.venda.VendaCreateRequest;
import org.example.support.dto.venda.VendaResponse;
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
    public Page<VendaResponse> listar(@RequestParam(required = false) VendaStatus status, Pageable pageable) {
        return vendaRepository.search(status, pageable).map(VendaResponse::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaResponse get(@PathVariable Long id) {
        return VendaResponse.from(vendaService.buscar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaResponse criar(@Valid @RequestBody VendaCreateRequest req) {
        Venda v = vendaService.criarVenda(req);
        return VendaResponse.from(v);
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaResponse pagar(@PathVariable Long id) {
        return VendaResponse.from(vendaService.pagar(id));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VendaResponse cancelar(@PathVariable Long id) {
        return VendaResponse.from(vendaService.cancelar(id));
    }
}
