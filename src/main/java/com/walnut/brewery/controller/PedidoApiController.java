package com.walnut.brewery.controller;

import com.walnut.brewery.model.Pedido;
import com.walnut.brewery.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoApiController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        try {
            if (pedido.getNome() == null || pedido.getNome().trim().isEmpty() ||
                pedido.getCpf() == null || pedido.getCpf().trim().isEmpty() ||
                pedido.getCerveja() == null || pedido.getTamanho() == null) {
                
                Map<String, String> erro = new HashMap<>();
                erro.put("mensagem", "Dados obrigatórios não preenchidos.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
            }

            Pedido novoPedido = pedidoService.processarESalvarPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);

        } catch (IllegalArgumentException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", "Ocorreu um erro no processamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirPedido(@PathVariable Long id) {
        try {
            pedidoService.excluirPedido(id);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Reserva excluída com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
        }
    }
}