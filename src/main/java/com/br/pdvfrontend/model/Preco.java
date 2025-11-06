package com.br.pdvfrontend.model;

import java.math.BigDecimal;
import java.util.Date;

public class Preco {

    private Long id;
    private Long produtoId;
    private BigDecimal valor;
    private Date dataAlteracao;
    private Date horaAlteracao;
    private String nomeProduto; // recebido do backend

    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public BigDecimal getValor() { return valor; }
    public Date getDataAlteracao() { return dataAlteracao; }
    public Date getHoraAlteracao() { return horaAlteracao; }
    public String getNomeProduto() { return nomeProduto; }

    public void setId(Long id) { this.id = id; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setDataAlteracao(Date dataAlteracao) { this.dataAlteracao = dataAlteracao; }
    public void setHoraAlteracao(Date horaAlteracao) { this.horaAlteracao = horaAlteracao; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
}
