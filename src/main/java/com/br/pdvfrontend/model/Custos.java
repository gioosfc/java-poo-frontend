package com.br.pdvfrontend.model;

import java.util.Date;

public class Custos {
    //atributos
    private double imposto;
    private double custoVariaveis;
    private Date dataProcessamento;
    private double margemLucro;
    private double custoFixo;

    //construtor
    public Custos (double imposto, double custoFixo, double custoVariaveis, double margemLucro, Date dataProcessamento){
        this.custoFixo = custoFixo;
        this.custoVariaveis = custoVariaveis;
        this.imposto = imposto;
        this.margemLucro = margemLucro;
        this.dataProcessamento = dataProcessamento;
    }

    //getters
    public Date getDataProcessamento() {
        return dataProcessamento;
    }

    public double getCustoFixo() {
        return custoFixo;
    }

    public double getCustoVariaveis() {
        return custoVariaveis;
    }

    public double getImposto() {
        return imposto;
    }

    public double getMargemLucro() {
        return margemLucro;
    }

    //setters
    public void setCustoFixo(double custoFixo) {
        this.custoFixo = custoFixo;
    }

    public void setCustoVariaveis(double custoVariaveis) {
        this.custoVariaveis = custoVariaveis;
    }

    public void setDataProcessamento(Date dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }

    public void setImposto(double imposto) {
        this.imposto = imposto;
    }

    public void setMargemLucro(double margemLucro) {
        this.margemLucro = margemLucro;
    }
}


