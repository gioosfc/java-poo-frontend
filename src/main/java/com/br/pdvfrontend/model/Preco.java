package com.br.pdvfrontend.model;

import java.math.BigDecimal;
import java.util.Date;

public class Preco {
    private BigDecimal valor;
    private Date dataAlteracao;
    private Date horaAlteracao;

    public Preco (BigDecimal valor, Date dataAlteracao, Date horaAlteracao){
        this.dataAlteracao = dataAlteracao;
        this.horaAlteracao = horaAlteracao;
        this.valor = valor;
    }

    //getters
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
