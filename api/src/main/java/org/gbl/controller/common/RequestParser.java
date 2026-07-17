package org.gbl.controller.common;

import spark.Request;

public abstract class RequestParser {

    public String getId(Request request) {
        final var id = request.params("id");
        if (id == null || id.isEmpty())
            throw new InvalidPayloadException("invalid id");
        return id;
    }
}
