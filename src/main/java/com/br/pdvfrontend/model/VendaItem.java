package com.br.pdvfrontend.model;

import java.math.BigDecimal;

public class VendaItem {
    private Long id;
    private Long bombaId;
    private String bombaNome;
    private Produto produto; // usa o mesmo Produto que você já tem
    private BigDecimal precoUnitario;
    private BigDecimal quantidade;
    private BigDecimal subtotal;

    public VendaItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBombaId() { return bombaId; }
    public void setBombaId(Long bombaId) { this.bombaId = bombaId; }

    public String getBombaNome() { return bombaNome; }
    public void setBombaNome(String bombaNome) { this.bombaNome = bombaNome; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
