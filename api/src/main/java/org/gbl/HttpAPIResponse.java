package org.gbl;

import org.json.JSONObject;

public record HttpAPIResponse(ResponseStatus status, String message, JSONObject data) {

    public static HttpAPIResponse ofSuccess(JSONObject data) {
        return new HttpAPIResponse(ResponseStatus.SUCCESS, null, data);
    }

    public static HttpAPIResponse empty() {
        return new HttpAPIResponse(ResponseStatus.SUCCESS, "", null);
    }

    public static HttpAPIResponse ofError(String message) {
        return new HttpAPIResponse(ResponseStatus.ERROR, message, null);
    }
}
