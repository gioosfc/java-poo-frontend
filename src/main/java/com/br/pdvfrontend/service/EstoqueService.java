package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Estoque;
import java.util.ArrayList;
import java.util.List;

public class EstoqueService {

    private static EstoqueService instance;
    private final List<Estoque> estoques;

    private EstoqueService() {
        estoques = new ArrayList<>();
    }

    public static synchronized EstoqueService getInstance() {
        if (instance == null) {
            instance = new EstoqueService();
        }
        return instance;
    }

    public void addEstoque(Estoque estoque) {
        estoques.add(estoque);
    }

    public List<Estoque> getEstoques() {
        return estoques;
    }

    public Estoque getEstoque(int index) {
        if (index >= 0 && index < estoques.size()) {
            return estoques.get(index);
        }
        return null;
    }
}
