package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.util.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

    private final HttpClient httpClient = new HttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final String apiPath = "/produtos";

    public List<Produto> listar() {
        try {
            String jsonResponse = httpClient.get(apiPath + "/all");

            return objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<Produto>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Produto buscarPorId(Long id) {
        try {
            String jsonResponse = httpClient.get(apiPath + "/" + id);
            return objectMapper.readValue(jsonResponse, Produto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Produto salvar(Produto produto) {
        try {
            String jsonInput = objectMapper.writeValueAsString(produto);
            String jsonResponse;

            if (produto.getId() == null) {
                jsonResponse = httpClient.post(apiPath, jsonInput);
            } else {
                jsonResponse = httpClient.put(apiPath + "/" + produto.getId(), jsonInput);
            }

            return objectMapper.readValue(jsonResponse, Produto.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletar(Long id) {
        httpClient.delete(apiPath + "/" + id);
    }
}
