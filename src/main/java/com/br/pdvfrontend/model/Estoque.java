package com.br.pdvfrontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Estoque {

    //atributos
    private Long id;
    private BigDecimal quantidade;
    private String localTanque;
    private String localEndereco;
    private String loteFabricacao;
    private Date dataDeValidade;

    //construtor
    public Estoque() {
    }

    public Estoque (BigDecimal quantidade, String localEndereco, String localTanque, String loteFabricacao, Date dataDeValidade){
        this.quantidade = quantidade;
        this.localEndereco = localEndereco;
        this.localTanque = localTanque;
        this.dataDeValidade = dataDeValidade;
        this.loteFabricacao = loteFabricacao;
    }

    //getters
    public Long getId() {
        return id;
    }

    public BigDecimal getQuantidade(){
        return quantidade;
    }

    public String getLocalTanque(){
        return localTanque;
    }

    public String getLocalEndereco(){
        return localEndereco;
    }

    public String getLoteFabricacao(){
        return  loteFabricacao;
    }

    public Date getDataDeValidade(){
        return dataDeValidade;
    }

    //setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantidade (BigDecimal quantidade){
        this.quantidade = quantidade;
    }

    public void setDataDeValidade(Date dataDeValidade) {
        this.dataDeValidade = dataDeValidade;
    }

    public void setLocalEndereco(String localEndereco) {
        this.localEndereco = localEndereco;
    }

    public void setLocalTanque(String localTanque) {
        this.localTanque = localTanque;
    }

    public void setLoteFabricacao(String loteFabricacao) {
        this.loteFabricacao = loteFabricacao;
    }
}
