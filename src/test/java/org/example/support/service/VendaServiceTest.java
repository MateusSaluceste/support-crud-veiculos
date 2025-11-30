package org.example.support.service;

import org.example.support.domain.entity.Cliente;
import org.example.support.domain.entity.ItemVenda;
import org.example.support.domain.entity.Veiculo;
import org.example.support.domain.entity.Venda;
import org.example.support.domain.enums.VendaStatus;
import org.example.support.dto.venda.ItemVendaEntrada;
import org.example.support.dto.venda.VendaEntrada;
import org.example.support.exception.BusinessException;
import org.example.support.repository.ClienteRepository;
import org.example.support.repository.VeiculoRepository;
import org.example.support.repository.VendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VendaServiceTest {

    private VendaRepository vendaRepository;
    private ClienteRepository clienteRepository;
    private VeiculoRepository veiculoRepository;
    private VendaService vendaService;

    @BeforeEach
    void setUp() {
        vendaRepository = Mockito.mock(VendaRepository.class);
        clienteRepository = Mockito.mock(ClienteRepository.class);
        veiculoRepository = Mockito.mock(VeiculoRepository.class);
        vendaService = new VendaService(vendaRepository, clienteRepository, veiculoRepository);
    }

    @Test
    void naoPermitePagarSemEstoque() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Veiculo veiculo = new Veiculo();
        veiculo.setId(10L);
        veiculo.setPlaca("ABC1D23");
        veiculo.setPrecoVendaSugerido(new BigDecimal("1000.00"));
        veiculo.setQuantidadeEmEstoque(0);
        when(veiculoRepository.findById(10L)).thenReturn(Optional.of(veiculo));

        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        VendaEntrada req = new VendaEntrada();
        req.clienteId = 1L;
        req.formaPagamento = org.example.support.domain.enums.FormaPagamento.PIX;
        ItemVendaEntrada item = new ItemVendaEntrada();
        item.veiculoId = 10L;
        item.quantidade = 1;
        req.itens = List.of(item);

        Venda venda = vendaService.registrarVenda(req);
        when(vendaRepository.findById(anyLong())).thenReturn(Optional.of(venda));

        BusinessException ex = assertThrows(BusinessException.class, () -> vendaService.pagar(99L));
        assertTrue(ex.getMessage().toLowerCase().contains("estoque"));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void pagarBaixaEstoque() {
        Venda venda = new Venda();
        venda.setId(5L);

        Veiculo veiculo = new Veiculo();
        veiculo.setId(10L);
        veiculo.setPlaca("AAA0A00");
        veiculo.setPrecoVendaSugerido(new BigDecimal("50000"));
        veiculo.setQuantidadeEmEstoque(2);

        ItemVenda item = new ItemVenda();
        item.setVeiculo(veiculo);
        item.setQuantidade(1);
        item.setPrecoUnitarioNoMomento(new BigDecimal("50000"));
        item.setSubtotal(new BigDecimal("50000"));
        venda.addItem(item);

        when(vendaRepository.findById(5L)).thenReturn(Optional.of(venda));
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        Venda pago = vendaService.pagar(5L);

        assertEquals(VendaStatus.PAGA, pago.getStatus());
        assertEquals(1, veiculo.getQuantidadeEmEstoque());
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    void cancelarDevolveEstoqueQuandoJaPago() {
        Venda venda = new Venda();
        venda.setId(7L);
        venda.setStatus(VendaStatus.PAGA);

        Veiculo veiculo = new Veiculo();
        veiculo.setId(33L);
        veiculo.setPlaca("BBB1B11");
        veiculo.setQuantidadeEmEstoque(1);

        ItemVenda item = new ItemVenda();
        item.setVeiculo(veiculo);
        item.setQuantidade(1);
        item.setPrecoUnitarioNoMomento(new BigDecimal("100"));
        item.setSubtotal(new BigDecimal("100"));
        venda.addItem(item);

        when(vendaRepository.findById(7L)).thenReturn(Optional.of(venda));
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        Venda cancelada = vendaService.cancelar(7L);

        assertEquals(VendaStatus.CANCELADA, cancelada.getStatus());
        assertEquals(2, veiculo.getQuantidadeEmEstoque());
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }
}
