package com.walnut.brewery.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cerveja;

    private Integer tamanho;

    private Integer quantidade;

    public Estoque() {
    }

    public Estoque(String cerveja, Integer tamanho, Integer quantidade) {
        this.cerveja = cerveja;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public String getCerveja() {
        return cerveja;
    }

    public void setCerveja(String cerveja) {
        this.cerveja = cerveja;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
