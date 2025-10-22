package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Custos;
import java.util.ArrayList;
import java.util.List;

public class CustoService {

    private static CustoService instance;
    private final List<Custos> custos;

    private CustoService() {
        custos = new ArrayList<>();
    }

    public static synchronized CustoService getInstance() {
        if (instance == null) {
            instance = new CustoService();
        }
        return instance;
    }

    public void addCusto(Custos custo) {
        custos.add(custo);
    }

    public List<Custos> getCustos() {
        return custos;
    }

    public Custos getCusto(int index) {
        if (index >= 0 && index < custos.size()) {
            return custos.get(index);
        }
        return null;
    }
}
