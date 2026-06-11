package com.walnut.brewery.service;

import com.walnut.brewery.model.Pedido;
import com.walnut.brewery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    private final Map<String, double[]> beerPrices = new HashMap<>();

    public PedidoService() {
        beerPrices.put("ipa", new double[]{450.0, 680.0});
        beerPrices.put("pilsen", new double[]{380.0, 580.0});
        beerPrices.put("stout", new double[]{500.0, 750.0});
        beerPrices.put("wheat", new double[]{420.0, 640.0});
        beerPrices.put("red-ale", new double[]{460.0, 700.0});
        beerPrices.put("porter", new double[]{480.0, 720.0});
    }

    public Pedido processarESalvarPedido(Pedido pedido) {
        String cervejaKey = pedido.getCerveja().toLowerCase();
        double[] precos = beerPrices.get(cervejaKey);
        
        if (precos == null) {
            throw new IllegalArgumentException("Rótulo de cerveja inválido.");
        }

        double valorBarril = (pedido.getTamanho() == 50) ? precos[1] : precos[0];
        double valorFrete = "entrega".equalsIgnoreCase(pedido.getTipoFrete()) ? 80.0 : 0.0;
        
        pedido.setValorTotal(valorBarril + valorFrete);
        
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public void excluirPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pedido não encontrado.");
        }
        pedidoRepository.deleteById(id);
    }
}