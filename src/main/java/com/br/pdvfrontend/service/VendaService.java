package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Venda;
import com.google.gson.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Serviço de integração com o backend para criação de vendas e download de comprovantes.
 */
public class VendaService {

    private static final String BASE_URL = "http://localhost:8080/api/v1/vendas";
    private final Gson gson;

    public VendaService() {
        gson = buildGson();
    }

    private Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // ✅ Serializador e desserializador para LocalDateTime
        builder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(dtf));
            }
        });
        builder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), dtf);
            }
        });

        // ✅ Também cobre LocalDate e LocalTime
        builder.registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, ctx) ->
                new JsonPrimitive(src.toString()));
        builder.registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, ctx) ->
                LocalDate.parse(json.getAsString()));

        builder.registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, type, ctx) ->
                new JsonPrimitive(src.toString()));
        builder.registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, ctx) ->
                LocalTime.parse(json.getAsString()));

        builder.setPrettyPrinting();
        return builder.create();
    }

    /**
     * Envia a venda ao backend e retorna a venda salva (com ID e total).
     */
    public Venda criarVenda(Venda venda) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String json = gson.toJson(venda);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300)
                    ? conn.getInputStream() : conn.getErrorStream();

            String resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (code >= 200 && code < 300) {
                return gson.fromJson(resp, Venda.class);
            } else {
                throw new RuntimeException("Erro HTTP " + code + ": " + resp);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar venda: " + e.getMessage(), e);
        }
    }

    /**
     * Faz download do comprovante PDF da venda.
     */
    public byte[] baixarComprovante(Long vendaId) {
        try {
            URL url = new URL(BASE_URL + "/" + vendaId + "/comprovante");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/pdf");

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300)
                    ? conn.getInputStream() : conn.getErrorStream();

            byte[] bytes = is.readAllBytes();
            if (code >= 200 && code < 300) {
                return bytes;
            } else {
                throw new RuntimeException("Erro ao baixar PDF: HTTP " + code);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao baixar comprovante: " + e.getMessage(), e);
        }
    }
}
