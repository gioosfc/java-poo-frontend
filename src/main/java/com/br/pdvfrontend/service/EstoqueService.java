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
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final String apiPath = "/estoques";

    public List<Estoque> listar() {
        try {
            String jsonResponse = httpClient.get(apiPath + "/all");

            return objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<Estoque>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Estoque buscarPorId(Long id) {
        try {
            String jsonResponse = httpClient.get(apiPath + "/" + id);
            return objectMapper.readValue(jsonResponse, Estoque.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Estoque salvar(Estoque estoque) {
        try {
            String jsonInput = objectMapper.writeValueAsString(estoque);
            String jsonResponse;

            if (estoque.getId() == null) {
                jsonResponse = httpClient.post(apiPath, jsonInput);
            } else {
                jsonResponse = httpClient.put(apiPath + "/" + estoque.getId(), jsonInput);
            }

            return objectMapper.readValue(jsonResponse, Estoque.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletar(Long id) {
        httpClient.delete(apiPath + "/" + id);
    }
}
