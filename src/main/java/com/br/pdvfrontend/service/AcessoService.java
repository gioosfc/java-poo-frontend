package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.util.HttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AcessoService {

    private final HttpClient http = new HttpClient();
    private final Gson gson = new Gson();

    public List<Acesso> listar() {
        try {
            String json = http.get("acessos/all");
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

    // ✅ MÉTODO LOGIN (fora do deletar)
    public Acesso login(String usuario, String senha) {
        try {
            URL url = new URL("http://localhost:8080/api/v1/acessos/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");

            String json = "{ \"usuario\": \"" + usuario + "\", \"senha\": \"" + senha + "\" }";
            con.getOutputStream().write(json.getBytes());

            if (con.getResponseCode() == 200) {
                String responseJson = new String(con.getInputStream().readAllBytes());
                return gson.fromJson(responseJson, Acesso.class);
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    }

