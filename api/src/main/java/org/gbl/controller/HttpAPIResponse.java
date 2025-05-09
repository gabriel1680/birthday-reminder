package org.gbl.controller;

import org.json.JSONObject;

public record HttpAPIResponse(ResponseStatus status, String message, Object data) {
    private static final String EMPTY_MESSAGE = "";
    private static final JSONObject EMPTY_DATA = new JSONObject();

    public static HttpAPIResponse ofSuccess(Object data) {
        return new HttpAPIResponse(ResponseStatus.SUCCESS, EMPTY_MESSAGE, data);
    }

    public static HttpAPIResponse empty() {
        return new HttpAPIResponse(ResponseStatus.SUCCESS, EMPTY_MESSAGE, EMPTY_DATA);
    }

    public static HttpAPIResponse ofError(String message) {
        return new HttpAPIResponse(ResponseStatus.ERROR, message, EMPTY_DATA);
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
