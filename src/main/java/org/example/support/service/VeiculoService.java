package org.example.support.service;

import org.example.support.domain.entity.Veiculo;
import org.example.support.domain.enums.StatusVeiculo;
import org.example.support.dto.veiculo.VeiculoRequest;
import org.example.support.exception.NotFoundException;
import org.example.support.repository.VeiculoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public Page<Veiculo> listar(String marca, String modelo, Integer ano, StatusVeiculo status, Pageable pageable) {
        return veiculoRepository.search(marca, modelo, ano, status, pageable);
    }

    public Veiculo buscar(Long id) {
        return veiculoRepository.findById(id).orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
    }

    @Transactional
    public Veiculo criar(VeiculoRequest req) {
        Veiculo v = new Veiculo();
        copy(req, v);
        try {
            return veiculoRepository.save(v);
        } catch (DataIntegrityViolationException e) {
            throw e; // tratado no advice -> 409
        }
    }

    @Transactional
    public Veiculo atualizar(Long id, VeiculoRequest req) {
        Veiculo v = buscar(id);
        copy(req, v);
        return veiculoRepository.save(v);
    }

    @Transactional
    public void deletar(Long id) {
        Veiculo v = buscar(id);
        v.setAtivo(false);
        v.setStatus(StatusVeiculo.INATIVO);
        veiculoRepository.save(v);
    }

    @Transactional
    public Veiculo ajustarEstoque(Long id, int novoEstoque) {
        Veiculo v = buscar(id);
        v.setQuantidadeEmEstoque(Math.max(0, novoEstoque));
        return veiculoRepository.save(v);
    }

    private void copy(VeiculoRequest req, Veiculo v) {
        v.setMarca(req.marca);
        v.setModelo(req.modelo);
        v.setAno(req.ano);
        v.setCor(req.cor);
        v.setPlaca(req.placa);
        v.setRenavam(req.renavam);
        v.setQuilometragem(req.quilometragem);
        v.setTipoCombustivel(req.tipoCombustivel);
        v.setCambio(req.cambio);
        v.setPrecoCompra(req.precoCompra);
        v.setPrecoVendaSugerido(req.precoVendaSugerido);
        v.setQuantidadeEmEstoque(req.quantidadeEmEstoque);
        v.setStatus(req.status);
        v.setAtivo(true);
    }
}
