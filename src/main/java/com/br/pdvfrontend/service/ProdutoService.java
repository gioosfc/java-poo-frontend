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
    private final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final String apiPath = "produtos";

    /**
     * ✅ Usado pela tela de ProdutoList (paginado manualmente via /all)
     */
    public List<Produto> listar() {
        try {
            String json = httpClient.get(apiPath + "/all");
            return mapper.readValue(json, new TypeReference<List<Produto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * ✅ Usado pelo EstoqueForm (combobox de produtos)
     */
    public List<Produto> listarTodos() {
        try {
            String json = httpClient.get(apiPath + "/all");
            return mapper.readValue(json, new TypeReference<List<Produto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Produto buscarPorId(Long id) {
        try {
            String json = httpClient.get(apiPath + "/" + id);
            return mapper.readValue(json, Produto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Produto salvar(Produto produto) {
        try {
            String jsonInput = mapper.writeValueAsString(produto);
            String jsonResponse;

            if (produto.getId() == null) {
                jsonResponse = httpClient.post(apiPath, jsonInput);
            } else {
                jsonResponse = httpClient.put(apiPath + "/" + produto.getId(), jsonInput);
            }

            return mapper.readValue(jsonResponse, Produto.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletar(Long id) {
        httpClient.delete(apiPath + "/" + id);
    }
}
