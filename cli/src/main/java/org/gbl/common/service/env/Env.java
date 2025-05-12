package org.gbl.common.service.env;

public enum Env {
    TEST("test"),
    DEV("development"),
    PROD("production");

    private final String value;

    Env(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Env of(String s) {
        for (var env : values()) {
            if (env.value.equals(s)) return env;
        }
        throw new IllegalArgumentException("Invalid Env value: \"%s\"".formatted(s));
    }
}
