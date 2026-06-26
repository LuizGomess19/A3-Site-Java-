package com.walnut.brewery.service;

import com.walnut.brewery.model.Pedido;
import com.walnut.brewery.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EstoqueService estoqueService;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCalcularValorTotalCorretamenteComFrete() {
        Pedido pedido = new Pedido();
        pedido.setCerveja("ipa");
        pedido.setTamanho(50);
        pedido.setTipoFrete("entrega");

      doNothing().when(estoqueService).verificarDisponibilidade(any(Pedido.class));
      doNothing().when(estoqueService).baixarEstoque(any(Pedido.class));

      when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

      pedidoService.processarESalvarPedido(pedido);

        // IPA 50L (680.0) + Entrega (80.0) = 760.0
        assertEquals(760.0, pedido.getValorTotal());
        verify(pedidoRepository, times(1)).save(pedido);
        verify(estoqueService).verificarDisponibilidade(pedido);
        verify(estoqueService).baixarEstoque(pedido);
    }

    @Test
    void deveCalcularValorTotalCorretamenteSemFrete() {
        Pedido pedido = new Pedido();
        pedido.setCerveja("pilsen");
        pedido.setTamanho(30);
        pedido.setTipoFrete("retirada");

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        pedidoService.processarESalvarPedido(pedido);

        // Pilsen 30L (380.0) + Retirada (0.0) = 380.0
        assertEquals(380.0, pedido.getValorTotal());
        verify(estoqueService).verificarDisponibilidade(pedido);
        verify(estoqueService).baixarEstoque(pedido);
 
        doNothing().when(estoqueService).verificarDisponibilidade(any(Pedido.class));
        doNothing().when(estoqueService).baixarEstoque(any(Pedido.class));

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
    }
    
    @Test
    void deveLancarExcecaoQuandoCervejaInvalida() {
        Pedido pedido = new Pedido();
        pedido.setCerveja("cerveja-inexistente");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.processarESalvarPedido(pedido);
        });

        assertEquals("Rótulo de cerveja inválido.", exception.getMessage());
    }

@Test
void deveLancarExcecaoQuandoNaoHaEstoque() {

    Pedido pedido = new Pedido();
    pedido.setCerveja("ipa");
    pedido.setTamanho(50);
    pedido.setTipoFrete("entrega");

    doThrow(new IllegalArgumentException("Produto indisponível no momento."))
            .when(estoqueService)
            .verificarDisponibilidade(any(Pedido.class));

    Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedidoService.processarESalvarPedido(pedido));

    assertEquals("Produto indisponível no momento.", exception.getMessage());

    verify(pedidoRepository, never()).save(any(Pedido.class));
}
    }
