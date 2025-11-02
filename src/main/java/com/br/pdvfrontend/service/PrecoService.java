package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.util.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrecoService {

    private final HttpClient httpClient = new HttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final String apiPath = "/precos";

    public List<Preco> listar() {
        try {
            String jsonResponse = httpClient.get(apiPath + "/all");

            return objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<Preco>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Preco buscarPorId(Long id) {
        try {
            String jsonResponse = httpClient.get(apiPath + "/" + id);
            return objectMapper.readValue(jsonResponse, Preco.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Preco salvar(Preco preco) {
        try {
            String jsonInput = objectMapper.writeValueAsString(preco);
            String jsonResponse;

            if (preco.getId() == null) {
                jsonResponse = httpClient.post(apiPath, jsonInput);
            } else {
                jsonResponse = httpClient.put(apiPath + "/" + preco.getId(), jsonInput);
            }

            return objectMapper.readValue(jsonResponse, Preco.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletar(Long id) {
        httpClient.delete(apiPath + "/" + id);
    }
}
