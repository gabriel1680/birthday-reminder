package org.gbl.common.service.json;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonJsonParser implements JsonParser {

    private static final Gson parser = new Gson();

    public String stringify(Object object) {
        return parser.toJson(object);
    }

    public <T> T parse(String json, Type typeOf) {
        return parser.fromJson(json, typeOf);
    }
}
