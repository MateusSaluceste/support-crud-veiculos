package org.example.support.controller;

import jakarta.validation.Valid;
import org.example.support.domain.entity.Cliente;
import org.example.support.dto.cliente.ClienteRequest;
import org.example.support.dto.cliente.ClienteResponse;
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
    public Page<ClienteResponse> listar(@RequestParam(required = false) String q, Pageable pageable) {
        return clienteService.listar(q, pageable).map(ClienteResponse::from);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ClienteResponse get(@PathVariable Long id) {
        return ClienteResponse.from(clienteService.buscar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest req) {
        Cliente c = clienteService.criar(req);
        return ClienteResponse.from(c);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req) {
        return ClienteResponse.from(clienteService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        clienteService.deletar(id);
    }
}
