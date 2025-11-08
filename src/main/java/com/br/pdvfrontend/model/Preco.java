package com.br.pdvfrontend.model;

import java.math.BigDecimal;

public class Preco {

    private Long id;
    private Long produtoId;
    private BigDecimal valor;
    private String dataAlteracao; // troquei de Date → String
    private String horaAlteracao; // troquei de Date → String
    private String nomeProduto;

    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public BigDecimal getValor() { return valor; }
    public String getDataAlteracao() { return dataAlteracao; }
    public String getHoraAlteracao() { return horaAlteracao; }
    public String getNomeProduto() { return nomeProduto; }

    public void setId(Long id) { this.id = id; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setDataAlteracao(String dataAlteracao) { this.dataAlteracao = dataAlteracao; }
    public void setHoraAlteracao(String horaAlteracao) { this.horaAlteracao = horaAlteracao; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
}
