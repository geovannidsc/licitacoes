package com.effecti.licitacoes.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "itens_edital")
public class ItemEdital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroItem;
    private String nome;
    private String descricao;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "licitacao_id", nullable = false)
    private Licitacao licitacao;


    public ItemEdital() {}

    public ItemEdital(String numeroItem, String nome, String descricao, Licitacao licitacao) {
        this.numeroItem = numeroItem;
        this.nome = nome;
        this.descricao = descricao;
        this.licitacao = licitacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroItem() {
        return numeroItem;
    }

    public void setNumeroItem(String numeroItem) {
        this.numeroItem = numeroItem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Licitacao getLicitacao() {
        return licitacao;
    }

    public void setLicitacao(Licitacao licitacao) {
        this.licitacao = licitacao;
    }

    @Override
    public String toString() {
        return "ItemEdital{" +
                "id=" + id +
                ", numeroItem='" + numeroItem + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", licitacao=" + licitacao +
                '}';
    }
}
