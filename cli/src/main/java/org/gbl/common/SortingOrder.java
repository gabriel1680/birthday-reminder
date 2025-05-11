package org.gbl.common;

public enum SortingOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortingOrder(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static SortingOrder of(String s) {
        for (var value : values()) {
            if (value.value.equals(s)) {
                return value;
            }
        }
        throw new IllegalArgumentException("invalid SortingOrder.class enum type");
    }
}
