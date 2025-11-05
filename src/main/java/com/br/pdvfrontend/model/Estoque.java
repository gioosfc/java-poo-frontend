package com.br.pdvfrontend.model;

import java.math.BigDecimal;

public class Estoque {
    private Long id;
    private Long produtoId;          // usado para salvar/atualizar
    private String produtoNome;      // vem na resposta para mostrar na tabela
    private String produtoReferencia;// idem
    private BigDecimal quantidade;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }

    public String getProdutoReferencia() { return produtoReferencia; }
    public void setProdutoReferencia(String produtoReferencia) { this.produtoReferencia = produtoReferencia; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
}
