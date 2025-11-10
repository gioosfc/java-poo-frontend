package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Venda;
import com.br.pdvfrontend.model.VendaItem;
import com.br.pdvfrontend.request.VendaRequest;
import com.br.pdvfrontend.util.OffsetDateTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de integração com o backend para criação de vendas e download de comprovantes.
 */
public class VendaService {

    private static final String BASE_URL = "http://localhost:8080/api/v1/vendas";
    private final Gson gson;

    public VendaService() {
        gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()) // <-- LINHA CHAVE
                .create();
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
    public Venda criarVenda(Venda venda, String placa, String forma) {
        try {
            VendaRequest vendaRqst = new VendaRequest();
            vendaRqst.setPlaca(placa);
            vendaRqst.setFormaPagamento(forma);

            for (VendaItem item : venda.getItens()){
                VendaRequest.Item itemRqst = new VendaRequest.Item();
                itemRqst.setProdutoId(item.getProduto().getId());
                itemRqst.setQuantidade(item.getQuantidade());
                itemRqst.setBombaId(item.getBombaId());
                itemRqst.setBombaNome(item.getBombaNome());
                vendaRqst.getItens().add(itemRqst);
            }


            URL url = new URL(BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String json = gson.toJson(vendaRqst);
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
            throw new RuntimeException(e.getMessage(), e);
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

    /** 🔎 Lista vendas do relatório (com filtros) */
    public List<Venda> listarVendas(LocalDate inicio, LocalDate fim, String forma, String placa) {
        try {
            String qs = String.format("?inicio=%s&fim=%s%s%s",
                    inicio.toString(),
                    fim.toString(),
                    (forma != null && !forma.isBlank() && !"TODAS".equalsIgnoreCase(forma))
                            ? "&forma=" + URLEncoder.encode(forma, StandardCharsets.UTF_8) : "",
                    (placa != null && !placa.isBlank())
                            ? "&placa=" + URLEncoder.encode(placa, StandardCharsets.UTF_8) : ""
            );

            URL url = new URL(BASE_URL + "/relatorio" + qs);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300)
                    ? conn.getInputStream() : conn.getErrorStream();

            String resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (code >= 200 && code < 300) {
                return gson.fromJson(resp, new TypeToken<List<Venda>>(){}.getType());
            } else {
                throw new RuntimeException("Erro HTTP " + code + ": " + resp);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar vendas: " + e.getMessage(), e);
        }
    }

    /** 📊 Resumo por produto (para a aba de resumo do relatório) */
    public List<ResumoProduto> resumoPorProduto(LocalDate inicio, LocalDate fim, String forma, String placa) {
        try {
            String qs = String.format("?inicio=%s&fim=%s%s%s",
                    inicio.toString(),
                    fim.toString(),
                    (forma != null && !forma.isBlank() && !"TODAS".equalsIgnoreCase(forma))
                            ? "&forma=" + URLEncoder.encode(forma, StandardCharsets.UTF_8) : "",
                    (placa != null && !placa.isBlank())
                            ? "&placa=" + URLEncoder.encode(placa, StandardCharsets.UTF_8) : ""
            );

            URL url = new URL(BASE_URL + "/resumo-produtos" + qs);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300)
                    ? conn.getInputStream() : conn.getErrorStream();

            String resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            if (code >= 200 && code < 300) {
                return gson.fromJson(resp, new TypeToken<List<ResumoProduto>>(){}.getType());
            } else {
                throw new RuntimeException("Erro HTTP " + code + ": " + resp);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter resumo por produto: " + e.getMessage(), e);
        }
    }

    /** DTO simples para o resumo vindo do backend */
    public static class ResumoProduto {
        private String produto;
        private java.math.BigDecimal litros;
        private java.math.BigDecimal total;

        public String getProduto() { return produto; }
        public java.math.BigDecimal getLitros() { return litros; }
        public java.math.BigDecimal getTotal() { return total; }
    }
}
