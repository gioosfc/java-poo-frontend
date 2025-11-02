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

    private final String apiPath = "/contato";

    public List<Contato> listar() {
        try {
            String jsonResponse = httpClient.get(apiPath + "/all");

            return objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<Contato>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Contato buscarPorId(Long id) {
        try {
            String jsonResponse = httpClient.get(apiPath + "/" + id);
            return objectMapper.readValue(jsonResponse, Contato.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Contato salvar(Contato contato) {
        try {
            String jsonInput = objectMapper.writeValueAsString(contato);
            String jsonResponse;

            if (contato.getId() == null) {
                jsonResponse = httpClient.post(apiPath, jsonInput);
            } else {
                jsonResponse = httpClient.put(apiPath + "/" + contato.getId(), jsonInput);
            }

            return objectMapper.readValue(jsonResponse, Contato.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //public void deletar(Long id) {
        //try {
            //httpClient.delete(apiPath + "/" + id);
        }//catch (InterruptedException e) {
           // e.printStackTrace();
       // }
    //}

    //public List<Object> listarTodos() {
      //  return null;
    //}
//}
