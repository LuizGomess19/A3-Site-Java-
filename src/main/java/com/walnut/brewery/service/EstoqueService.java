package com.walnut.brewery.service;

import com.walnut.brewery.model.Estoque;
import com.walnut.brewery.model.Pedido;
import com.walnut.brewery.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    public void verificarDisponibilidade(Pedido pedido) {

        Estoque estoque = estoqueRepository
                .findByCervejaAndTamanho(
                        pedido.getCerveja(),
                        pedido.getTamanho())
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado no estoque."));

        if (estoque.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Produto indisponível no momento.");
        }
    }

    public void baixarEstoque(Pedido pedido) {

        Estoque estoque = estoqueRepository
                .findByCervejaAndTamanho(
                        pedido.getCerveja(),
                        pedido.getTamanho())
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado no estoque."));

        estoque.setQuantidade(estoque.getQuantidade() - 1);

        estoqueRepository.save(estoque);
    }
}
