package org.gbl.common.service.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.gbl.common.search.GsonPaginationDeserializer;
import org.gbl.common.search.Pagination;
import org.gbl.common.gateway.ContactResponse;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class GsonJsonServiceAdapter implements JsonService {

    private static final Gson parser = new GsonBuilder()
            .registerTypeAdapter(
                    new TypeToken<Pagination<ContactResponse>>() {}.getType(),
                    new GsonPaginationDeserializer<ContactResponse>()
            )
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .create();

    public String stringify(Object object) {
        return parser.toJson(object);
    }

    public <T> T parse(String json, Type typeOf) {
        return parser.fromJson(json, typeOf);
    }
}
