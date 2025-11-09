package com.br.pdvfrontend.model;

import java.math.BigDecimal;

public class Preco {
    private Long id;
    private Long produtoId;
    private BigDecimal valor;
    private String dataAlteracao;
    private String nomeProduto;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getDataAlteracao() { return dataAlteracao; }
    public void setDataAlteracao(String dataAlteracao) { this.dataAlteracao = dataAlteracao; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    @Override
    public String toString() {
        return nomeProduto + " - R$ " + valor;
    }
}
