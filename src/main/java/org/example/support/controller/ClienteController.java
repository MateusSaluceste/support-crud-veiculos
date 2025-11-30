package org.example.support.controller;

import jakarta.validation.Valid;
import org.example.support.domain.entity.Cliente;
import org.example.support.dto.cliente.ClienteEntrada;
import org.example.support.dto.cliente.ClienteSaida;
import org.example.support.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public Page<ClienteSaida> listar(@RequestParam(required = false) String q, Pageable pageable) {
        return clienteService.listar(q, pageable).map(ClienteSaida::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ClienteSaida obter(@PathVariable Long id) {
        return ClienteSaida.from(clienteService.obterPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ClienteSaida criar(@Valid @RequestBody ClienteEntrada req) {
        Cliente c = clienteService.criar(req);
        return ClienteSaida.from(c);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ClienteSaida atualizar(@PathVariable Long id, @Valid @RequestBody ClienteEntrada req) {
        return ClienteSaida.from(clienteService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        clienteService.deletar(id);
    }
}
