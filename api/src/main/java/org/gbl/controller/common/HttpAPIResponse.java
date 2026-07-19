package org.gbl.controller.common;

import org.json.JSONObject;

import java.util.Map;

public record HttpAPIResponse(ResponseStatus status, String message, Object data) {
    private static final String EMPTY_MESSAGE = "";
    private static final Map<String, Object> EMPTY_DATA = Map.of();

    public static HttpAPIResponse ofSuccess(Object data) {
        return new HttpAPIResponse(ResponseStatus.SUCCESS, EMPTY_MESSAGE, data);
    }

    public static HttpAPIResponse empty() {
        return new HttpAPIResponse(ResponseStatus.SUCCESS, EMPTY_MESSAGE, EMPTY_DATA);
    }

    public static HttpAPIResponse ofError(String message) {
        return new HttpAPIResponse(ResponseStatus.ERROR, message, EMPTY_DATA);
    }

    public static HttpAPIResponse ofSuccess() {
        return ofSuccess(null);
    }

    @Override
    public String toString() {
        return new JSONObject()
                .put("status", status.value())
                .put("message", message)
                .put("data", data)
                .toString();
    }
}
