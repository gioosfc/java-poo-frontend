package com.br.pdvfrontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Preco {
    private Long id;
    private BigDecimal valor;
    private Date dataAlteracao;
    private Date horaAlteracao;

    public Preco() {
    }

    public Preco (BigDecimal valor, Date dataAlteracao, Date horaAlteracao){
        this.dataAlteracao = dataAlteracao;
        this.horaAlteracao = horaAlteracao;
        this.valor = valor;
    }

    //getters
    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public Date getHoraAlteracao() {
        return horaAlteracao;
    }

    //setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public void setHoraAlteracao(Date horaAlteracao) {
        this.horaAlteracao = horaAlteracao;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
