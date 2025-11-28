package org.example.support.controller;

import jakarta.validation.Valid;
import org.example.support.domain.entity.Veiculo;
import org.example.support.domain.enums.StatusVeiculo;
import org.example.support.dto.veiculo.VeiculoRequest;
import org.example.support.dto.veiculo.VeiculoResponse;
import org.example.support.service.VeiculoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {
    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public Page<VeiculoResponse> listar(@RequestParam(required = false) String marca,
                                        @RequestParam(required = false) String modelo,
                                        @RequestParam(required = false) Integer ano,
                                        @RequestParam(required = false) StatusVeiculo status,
                                        Pageable pageable) {
        return veiculoService.listar(marca, modelo, ano, status, pageable).map(VeiculoResponse::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public VeiculoResponse get(@PathVariable Long id) {
        return VeiculoResponse.from(veiculoService.buscar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public VeiculoResponse criar(@Valid @RequestBody VeiculoRequest req) {
        Veiculo v = veiculoService.criar(req);
        return VeiculoResponse.from(v);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VeiculoResponse atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoRequest req) {
        return VeiculoResponse.from(veiculoService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        veiculoService.deletar(id);
    }

    @PatchMapping("/{id}/estoque")
    @PreAuthorize("hasRole('ADMIN')")
    public VeiculoResponse ajustarEstoque(@PathVariable Long id, @RequestParam int quantidade) {
        return VeiculoResponse.from(veiculoService.ajustarEstoque(id, quantidade));
    }
}
