package org.gbl.controller;

import io.javalin.http.Context;

import java.util.Map;

public abstract class JavalinController {

    protected String idFrom(Context context) {
        return context.pathParam("id");
    }

    protected Map<String, Object> toViewModelMap(Object viewModel) {
        return Map.of("viewModel", viewModel);
    }
}
