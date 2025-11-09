package com.br.pdvfrontend.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class VendaRequest {

    private String formaPagamento;
    private String placa;

    @NotNull
    private List<Item> itens;

    public VendaRequest()
    {
        itens = new java.util.ArrayList<>();
    }


    // ======= Getters e Setters =======

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    // ======= Classe interna Item =======

    public static class Item {
        @NotNull
        private Long produtoId;

        @NotNull
        private BigDecimal quantidade;

        private Long bombaId; // 🔹 opcional, mas útil se você diferencia por bomba

        // Getters e Setters
        public Long getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(Long produtoId) {
            this.produtoId = produtoId;
        }

        public BigDecimal getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
        }

        public Long getBombaId() {
            return bombaId;
        }

        public void setBombaId(Long bombaId) {
            this.bombaId = bombaId;
        }
    }
}
