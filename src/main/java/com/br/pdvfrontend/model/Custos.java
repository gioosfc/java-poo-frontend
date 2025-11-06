package com.br.pdvfrontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Custos {

    private Long id;
    private Long produtoId;
    private double imposto;
    private double custoVariaveis;
    private double margemLucro;
    private double custoFixo;
    private Date dataProcessamento;

    private String nomeProduto; // ✅ acrescentado

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public double getImposto() { return imposto; }
    public void setImposto(double imposto) { this.imposto = imposto; }

    public double getCustoVariaveis() { return custoVariaveis; }
    public void setCustoVariaveis(double custoVariaveis) { this.custoVariaveis = custoVariaveis; }

    public double getMargemLucro() { return margemLucro; }
    public void setMargemLucro(double margemLucro) { this.margemLucro = margemLucro; }

    public double getCustoFixo() { return custoFixo; }
    public void setCustoFixo(double custoFixo) { this.custoFixo = custoFixo; }

    public Date getDataProcessamento() { return dataProcessamento; }
    public void setDataProcessamento(Date dataProcessamento) { this.dataProcessamento = dataProcessamento; }

    public String getNomeProduto() { return nomeProduto; }  // ✅ getter
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; } // ✅ setter
}
