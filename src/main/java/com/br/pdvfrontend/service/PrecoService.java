package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Preco;
import java.util.ArrayList;
import java.util.List;

public class PrecoService {

    private static PrecoService instance;
    private final List<Preco> precos;

    private PrecoService() {
        precos = new ArrayList<>();
    }

    public static synchronized PrecoService getInstance() {
        if (instance == null) {
            instance = new PrecoService();
        }
        return instance;
    }

    public void addPreco(Preco preco) {
        precos.add(preco);
    }

    public List<Preco> getPrecos() {
        return precos;
    }

    public Preco getPreco(int index) {
        if (index >= 0 && index < precos.size()) {
            return precos.get(index);
        }
        return null;
    }
}
