package org.gbl.common.search;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class GsonPaginationDeserializer<T> implements JsonDeserializer<Pagination<T>> {

    @Override
    public Pagination<T> deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {
        final var data = json.getAsJsonObject();
        int page = data.get("current_page").getAsInt();
        int size = data.get("size").getAsInt();
        int total = data.get("total").getAsInt();
        int lastPage = data.get("last_page").getAsInt();
        final Collection<T> values = getValuesFrom((ParameterizedType) typeOfT, context, data);
        return new Pagination<>(page, size, total, lastPage, values);
    }

    private static <T> Collection<T> getValuesFrom(ParameterizedType typeOfT,
                                                   JsonDeserializationContext context,
                                                   JsonObject data) {
        final var itemType = typeOfT.getActualTypeArguments()[0];
        final var collectionType = TypeToken.getParameterized(Collection.class, itemType).getType();
        return context.deserialize(data.get("values"), collectionType);
    }
}