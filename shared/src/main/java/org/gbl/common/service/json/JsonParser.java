package org.gbl.common.service.json;

import java.lang.reflect.Type;

public interface JsonParser {
    String stringify(Object object);

    <T> T parse(String json, Type typeOf);
}
