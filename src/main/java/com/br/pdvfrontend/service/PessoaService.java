package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Pessoa;
import com.br.pdvfrontend.util.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PessoaService {

    private final HttpClient httpClient = new HttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Trocado Gson por ObjectMapper
    private final String apiPath = "/pessoas";

    public List<Pessoa> listar() {
        try {
            String jsonResponse = httpClient.get(apiPath);
            // Trocado a forma de obter a lista para o padrão do Jackson
            return objectMapper.readValue(jsonResponse, new TypeReference<ArrayList<Pessoa>>() {});
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Pessoa buscarPorId(Long id) {
        try {
            String jsonResponse = httpClient.get(apiPath + "/" + id);
            return objectMapper.readValue(jsonResponse, Pessoa.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pessoa salvar(Pessoa pessoa) {
        try {
            String jsonInput = objectMapper.writeValueAsString(pessoa); // Trocado toJson por writeValueAsString
            String jsonResponse;
            if (pessoa.getId() == null) { // Novo cadastro (POST)
                jsonResponse = httpClient.post(apiPath, jsonInput);
            } else { // Atualização (PUT)
                jsonResponse = httpClient.put(apiPath + "/" + pessoa.getId(), jsonInput);
            }
            return objectMapper.readValue(jsonResponse, Pessoa.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletar(Long id) {
        try {
            httpClient.delete(apiPath + "/" + id);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
