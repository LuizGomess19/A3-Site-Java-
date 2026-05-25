package com.walnut.brewery.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String celular;

    @Column(nullable = false)
    private String cerveja; // ipa, pilsen, stout, wheat, red-ale, porter

    @Column(nullable = false)
    private Integer tamanho; // 30 ou 50 litros

    @Column(nullable = false)
    private LocalDate dataRetirada;

    @Column(nullable = false)
    private LocalDate dataEntrega;

    @Column(nullable = false)
    private String horario;

    @Column(nullable = false)
    private String tipoFrete; // retirada ou entrega

    private String local; // endereco de entrega ou observacoes

    @Column(nullable = false)
    private Double valorTotal;

    @Column(name = "data_pedido", nullable = false, updatable = false)
    private LocalDateTime dataPedido;

    // Define automaticamente a data de criacao do pedido antes de salvar no banco
    @PrePersist
    protected void onCreate() {
        this.dataPedido = LocalDateTime.now();
    }
}
