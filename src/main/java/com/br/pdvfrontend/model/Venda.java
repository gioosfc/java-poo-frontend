package com.br.pdvfrontend.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class Venda {
    private Long id;
    private OffsetDateTime dataHora;
    private String usuario;
    private String formaPagamento;
    private String placa;
    private BigDecimal total;
    private List<VendaItem> itens;

    public Venda() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OffsetDateTime getDataHora() { return dataHora; }
    public void setDataHora(OffsetDateTime dataHora) { this.dataHora = dataHora; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<VendaItem> getItens() { return itens; }
    public void setItens(List<VendaItem> itens) { this.itens = itens; }
}
