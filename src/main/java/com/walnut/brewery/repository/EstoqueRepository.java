package com.walnut.brewery.repository;

import com.walnut.brewery.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByCervejaAndTamanho(String cerveja, Integer tamanho);

}
