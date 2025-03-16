package org.gbl;

public enum ResponseStatus {
    SUCCESS("success"),
    ERROR("error");

    private final String value;

    ResponseStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
