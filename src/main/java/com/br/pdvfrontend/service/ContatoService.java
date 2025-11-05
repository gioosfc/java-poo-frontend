package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Contato;
import com.br.pdvfrontend.util.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContatoService {

    private final HttpClient httpClient = new HttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final String apiPath = "contato";

    // ---------------- LISTAR ----------------
    public List<Contato> listar() {
        try {
            String jsonResponse = httpClient.get(apiPath);

            return objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<Contato>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ---------------- BUSCAR POR ID ----------------
    public Contato buscarPorId(Long id) {
        try {
            String jsonResponse = httpClient.get(apiPath + "/" + id);
            return objectMapper.readValue(jsonResponse, Contato.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------- SALVAR (POST / PUT) ----------------
    public Contato salvar(Contato contato) {
        try {
            String jsonInput = objectMapper.writeValueAsString(contato);
            String jsonResponse;

            // Criar
            if (contato.getId() == null) {
                jsonResponse = httpClient.post(apiPath, jsonInput);

                // Atualizar
            } else {
                jsonResponse = httpClient.put(apiPath + "/" + contato.getId(), jsonInput);
            }

            // 🔥 Backend não retorna nada → apenas confirma
            if (jsonResponse == null || jsonResponse.isBlank()) {
                return contato;
            }

            // Caso o backend passe a retornar JSON, já estamos prontos
            return objectMapper.readValue(jsonResponse, Contato.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------- DELETAR ----------------
    public void deletar(Long id) {
        httpClient.delete(apiPath + "/" + id);
    }
}
