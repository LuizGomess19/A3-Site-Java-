package com.walnut.brewery.repository;

import com.walnut.brewery.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Todos os metodos CRUD (save, findAll, deleteById, etc.) sao herdados automaticamente
}
