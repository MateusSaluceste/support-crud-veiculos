package org.example.support.service;

import org.example.support.domain.entity.Cliente;
import org.example.support.dto.cliente.ClienteEntrada;
import org.example.support.exception.NotFoundException;
import org.example.support.repository.ClienteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Page<Cliente> listar(String q, Pageable pageable) {
        return clienteRepository.search(q, pageable);
    }

    public Cliente obterPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }

    @Transactional
    public Cliente criar(ClienteEntrada req) {
        Cliente c = new Cliente();
        preencherDados(req, c);
        try {
            return clienteRepository.save(c);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteEntrada req) {
        Cliente c = obterPorId(id);
        preencherDados(req, c);
        return clienteRepository.save(c);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente c = obterPorId(id);
        c.setAtivo(false);
        clienteRepository.save(c);
    }

    private void preencherDados(ClienteEntrada req, Cliente c) {
        c.setNome(req.nome);
        c.setCpf(req.cpf);
        c.setEmail(req.email);
        c.setTelefone(req.telefone);
        c.setEndereco(req.endereco);
        c.setAtivo(true);
    }
}
