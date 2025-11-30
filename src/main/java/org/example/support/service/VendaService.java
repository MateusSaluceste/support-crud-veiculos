package org.example.support.service;

import org.example.support.domain.entity.Cliente;
import org.example.support.domain.entity.ItemVenda;
import org.example.support.domain.entity.Veiculo;
import org.example.support.domain.entity.Venda;
import org.example.support.domain.enums.VendaStatus;
import org.example.support.dto.venda.ItemVendaEntrada;
import org.example.support.dto.venda.VendaEntrada;
import org.example.support.exception.BusinessException;
import org.example.support.exception.NotFoundException;
import org.example.support.repository.ClienteRepository;
import org.example.support.repository.VeiculoRepository;
import org.example.support.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    public VendaService(VendaRepository vendaRepository, ClienteRepository clienteRepository, VeiculoRepository veiculoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional
    public Venda registrarVenda(VendaEntrada request) {
        List<ItemVendaEntrada> itensReq = request.itens;
        if (itensReq == null || itensReq.isEmpty()) {
            throw new BusinessException("Venda deve conter ao menos um item");
        }
        Cliente cliente = clienteRepository.findById(request.clienteId).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setFormaPagamento(request.formaPagamento);
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVendaEntrada ir : itensReq) {
            Veiculo veiculo = veiculoRepository.findById(ir.veiculoId).orElseThrow(() -> new NotFoundException("Veículo não encontrado: " + ir.veiculoId));
            ItemVenda item = new ItemVenda();
            item.setVeiculo(veiculo);
            item.setQuantidade(ir.quantidade);
            var preco = ir.precoUnitario != null ? ir.precoUnitario : veiculo.getPrecoVendaSugerido();
            item.setPrecoUnitarioNoMomento(preco);
            item.setSubtotal(preco.multiply(BigDecimal.valueOf(ir.quantidade)));
            venda.addItem(item);
            total = total.add(item.getSubtotal());
        }
        venda.setValorTotal(total);
        return vendaRepository.save(venda);
    }

    public Venda obterPorId(Long id) {
        return vendaRepository.findById(id).orElseThrow(() -> new NotFoundException("Venda não encontrada"));
    }

    @Transactional
    public Venda pagar(Long id) {
        Venda venda = obterPorId(id);
        if (venda.getStatus() == VendaStatus.PAGA) {
            return venda;
        }
        if (venda.getItens().isEmpty()) {
            throw new BusinessException("Venda sem itens não pode ser paga");
        }
        venda.getItens().forEach(item -> {
            Veiculo v = item.getVeiculo();
            if (v.getQuantidadeEmEstoque() < item.getQuantidade()) {
                throw new BusinessException("Estoque insuficiente para veículo placa " + v.getPlaca());
            }
        });
        venda.getItens().forEach(item -> {
            Veiculo v = item.getVeiculo();
            v.setQuantidadeEmEstoque(v.getQuantidadeEmEstoque() - item.getQuantidade());
            veiculoRepository.save(v);
        });
        venda.setStatus(VendaStatus.PAGA);
        return vendaRepository.save(venda);
    }

    @Transactional
    public Venda cancelar(Long id) {
        Venda venda = obterPorId(id);
        if (venda.getStatus() == VendaStatus.CANCELADA) return venda;
        if (venda.getStatus() == VendaStatus.PAGA) {
            venda.getItens().forEach(item -> {
                Veiculo v = item.getVeiculo();
                v.setQuantidadeEmEstoque(v.getQuantidadeEmEstoque() + item.getQuantidade());
                veiculoRepository.save(v);
            });
        }
        venda.setStatus(VendaStatus.CANCELADA);
        return vendaRepository.save(venda);
    }
}
