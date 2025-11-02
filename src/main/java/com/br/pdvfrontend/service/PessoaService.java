package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Pessoa;
import com.br.pdvfrontend.util.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PessoaService {

    private final HttpClient httpClient = new HttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()                       // registra JavaTimeModule, etc.
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // usa "yyyy-MM-dd"

    // ✅ Ponto base da API
    private final String apiPath = "/pessoas";

    public List<Pessoa> listar() {
        try {
            // ✅ Agora chama o endpoint correto que retorna LISTA
            String jsonResponse = httpClient.get(apiPath + "/all");

            return objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<Pessoa>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Pessoa buscarPorId(Long id) {
        try {
            String jsonResponse = httpClient.get(apiPath + "/" + id);
            return objectMapper.readValue(jsonResponse, Pessoa.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pessoa salvar(Pessoa pessoa) {
        try {
            String jsonInput = objectMapper.writeValueAsString(pessoa);
            String jsonResponse;

            if (pessoa.getId() == null) {
                jsonResponse = httpClient.post(apiPath, jsonInput);
            } else {
                jsonResponse = httpClient.put(apiPath + "/" + pessoa.getId(), jsonInput);
            }

            return objectMapper.readValue(jsonResponse, Pessoa.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletar(Long id) {
        httpClient.delete(apiPath + "/" + id);
    }
}
