package org.gbl.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JSON {

    private static final Gson parser = new Gson();

    public static String stringify(Object object) {
        return parser.toJson(object);
    }

    public static <T> T parse(String json, Type typeOf) {
        return parser.fromJson(json, typeOf);
    }
}
