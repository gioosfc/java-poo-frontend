package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.util.HttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AcessoService {

    private final HttpClient http = new HttpClient();
    private final Gson gson = new Gson();

    public List<Acesso> listar() {
        try {
            String json = http.get("acesso/all");
            Type type = new TypeToken<List<Acesso>>() {}.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Acesso buscarPorId(Long id) {
        try {
            String json = http.get("acesso/" + id);
            return gson.fromJson(json, Acesso.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void salvar(Acesso acesso) {
        try {
            String body = gson.toJson(acesso);
            http.post("acesso", body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Long id, Acesso acesso) {
        try {
            String body = gson.toJson(acesso);
            http.put("acesso/" + id, body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(Long id) {
        try {
            http.delete("acesso/" + id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
