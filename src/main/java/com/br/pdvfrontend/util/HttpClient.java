package com.br.pdvfrontend.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1/";

    public String get(String path) {
        try {
            URL url = new URL(BASE_URL + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            return readResponse(conn);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String post(String path, String json) {
        try {
            URL url = new URL(BASE_URL + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            writeBody(conn, json);
            return readResponse(conn);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String put(String path, String json) {
        try {
            URL url = new URL(BASE_URL + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            writeBody(conn, json);
            return readResponse(conn);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String path) {
        try {
            URL url = new URL(BASE_URL + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");
            conn.getResponseCode();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeBody(HttpURLConnection conn, String json) throws Exception {
        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes());
        os.flush();
        os.close();
    }

    private String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        StringBuilder response = new StringBuilder();

        while ((output = br.readLine()) != null) {
            response.append(output);
        }

        conn.disconnect();
        return response.toString();
    }
}
