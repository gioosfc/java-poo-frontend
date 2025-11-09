package com.br.pdvfrontend.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.OffsetDateTime;

/**
 * Ensina o Gson a ler e escrever OffsetDateTime.
 * Ele simplesmente usa os métodos padrão .parse() e .toString()
 */
public class OffsetDateTimeAdapter extends TypeAdapter<OffsetDateTime> {

    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        // Converte o objeto para String
        out.value(value.toString());
    }

    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        // Converte a String do JSON para o objeto
        String stringValue = in.nextString();
        return OffsetDateTime.parse(stringValue);
    }
}