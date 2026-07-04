package org.gbl.common.service.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer implements JsonSerializer<LocalDate>,
        JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public JsonElement serialize(LocalDate localDate, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDate.format(formatter)); // "yyyy-MM-dd"

    }

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type,
                                 JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(), formatter);
    }
}
