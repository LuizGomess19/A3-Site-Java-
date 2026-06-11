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

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        pedidoService.processarESalvarPedido(pedido);

        // IPA 50L (680.0) + Entrega (80.0) = 760.0
        assertEquals(760.0, pedido.getValorTotal());
        verify(pedidoRepository, times(1)).save(pedido);
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
}