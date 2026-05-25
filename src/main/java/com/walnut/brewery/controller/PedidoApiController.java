package com.walnut.brewery.controller;

import com.walnut.brewery.model.Pedido;
import com.walnut.brewery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*") // Permite chamadas de origens externas caso necessário
public class PedidoApiController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Endereço de cálculo com base nos preços oficiais definidos no frontend
    private final Map<String, double[]> beerPrices = new HashMap<>();

    public PedidoApiController() {
        // Índices: [0] = Barril 30L, [1] = Barril 50L
        beerPrices.put("ipa", new double[]{450.0, 680.0});
        beerPrices.put("pilsen", new double[]{380.0, 580.0});
        beerPrices.put("stout", new double[]{500.0, 750.0});
        beerPrices.put("wheat", new double[]{420.0, 640.0});
        beerPrices.put("red-ale", new double[]{460.0, 700.0});
        beerPrices.put("porter", new double[]{480.0, 720.0});
    }

    /**
     * Endpoint para salvar uma nova reserva de chopp
     */
    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        try {
            // 1. Validação básica de segurança
            if (pedido.getNome() == null || pedido.getNome().trim().isEmpty() ||
                pedido.getCpf() == null || pedido.getCpf().trim().isEmpty() ||
                pedido.getCerveja() == null || pedido.getTamanho() == null) {
                
                Map<String, String> erro = new HashMap<>();
                erro.put("mensagem", "Dados obrigatórios não preenchidos.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
            }

            // 2. Cálculo do valor total no servidor (por segurança)
            String cervejaKey = pedido.getCerveja().toLowerCase();
            double[] precos = beerPrices.get(cervejaKey);
            if (precos == null) {
                Map<String, String> erro = new HashMap<>();
                erro.put("mensagem", "Rótulo de cerveja inválido.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
            }

            double valorBarril = (pedido.getTamanho() == 50) ? precos[1] : precos[0];
            double valorFrete = "entrega".equalsIgnoreCase(pedido.getTipoFrete()) ? 80.0 : 0.0;
            double valorTotal = valorBarril + valorFrete;

            pedido.setValorTotal(valorTotal);

            // 3. Salvar no Banco H2
            Pedido novoPedido = pedidoRepository.save(pedido);

            // 4. Retornar dados salvos com status 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);

        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", "Ocorreu um erro no processamento da reserva: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Endpoint para listar todos os pedidos
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Endpoint para excluir uma reserva
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirPedido(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Pedido não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }

        pedidoRepository.deleteById(id);
        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Reserva excluída com sucesso.");
        return ResponseEntity.ok(resposta);
    }
}
