package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Estoque;
import com.br.pdvfrontend.util.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueService {

    private final HttpClient httpClient = new HttpClient();
    private final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final String apiPath = "estoques";

    public List<Estoque> listar() {
        try {
            String json = httpClient.get(apiPath + "/all"); // facilita no Swing
            return mapper.readValue(json, new TypeReference<List<Estoque>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Estoque buscarPorId(Long id) {
        try {
            String json = httpClient.get(apiPath + "/" + id);
            return mapper.readValue(json, Estoque.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Estoque salvar(Estoque e) {
        try {
            String payload = mapper.writeValueAsString(e);
            String json;
            if (e.getId() == null) {
                json = httpClient.post(apiPath, payload);
            } else {
                json = httpClient.put(apiPath + "/" + e.getId(), payload);
            }
            if (json == null || json.isBlank()) return null;
            return mapper.readValue(json, Estoque.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void deletar(Long id) {
        httpClient.delete(apiPath + "/" + id);
    }
}
