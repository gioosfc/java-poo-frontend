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

    // ✅ mantém como você usa hoje, porque seu HttpClient já prefixa /api/v1/
    private final String apiPath = "produtos";

    public List<Produto> listar() {
        String endpoint = apiPath + "/all";
        try {
            System.out.println("🔎 [ProdutoService] GET -> " + endpoint);
            String json = httpClient.get(endpoint);   // seu HttpClient deve montar BASE + endpoint
            System.out.println("📦 [ProdutoService] JSON <- " + json);
            return mapper.readValue(json, new TypeReference<List<Produto>>() {});
        } catch (IOException e) {
            // Pare de “mascarar” a falha como lista vazia; propague erro para você ver na UI/log
            throw new RuntimeException("Falha ao listar produtos em '" + endpoint + "': " + e.getMessage(), e);
        }
    }

    public List<Produto> listarTodos() {
        String endpoint = apiPath + "/all";
        try {
            System.out.println("🔎 [ProdutoService] GET -> " + endpoint);
            String json = httpClient.get(endpoint);
            System.out.println("📦 [ProdutoService] JSON <- " + json);
            return mapper.readValue(json, new TypeReference<List<Produto>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Falha ao listar todos os produtos em '" + endpoint + "': " + e.getMessage(), e);
        }
    }

    public Produto buscarPorId(Long id) {
        String endpoint = apiPath + "/" + id;
        try {
            System.out.println("🔎 [ProdutoService] GET -> " + endpoint);
            String json = httpClient.get(endpoint);
            System.out.println("📦 [ProdutoService] JSON <- " + json);
            return mapper.readValue(json, Produto.class);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao buscar produto em '" + endpoint + "': " + e.getMessage(), e);
        }
    }

    public Produto salvar(Produto produto) {
        try {
            String body = mapper.writeValueAsString(produto);
            String endpoint;
            String jsonResponse;

            if (produto.getId() == null) {
                endpoint = apiPath;
                System.out.println("🆕 [ProdutoService] POST -> " + endpoint + " | body=" + body);
                jsonResponse = httpClient.post(endpoint, body);
            } else {
                endpoint = apiPath + "/" + produto.getId();
                System.out.println("✏️ [ProdutoService] PUT -> " + endpoint + " | body=" + body);
                jsonResponse = httpClient.put(endpoint, body);
            }

            System.out.println("📦 [ProdutoService] JSON <- " + jsonResponse);
            return mapper.readValue(jsonResponse, Produto.class);

        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar produto: " + e.getMessage(), e);
        }
    }

    public void deletar(Long id) {
        String endpoint = apiPath + "/" + id;
        System.out.println("🗑 [ProdutoService] DELETE -> " + endpoint);
        httpClient.delete(endpoint);
    }
}
