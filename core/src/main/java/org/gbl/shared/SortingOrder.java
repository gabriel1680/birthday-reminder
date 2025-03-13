package org.gbl.shared;

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
}
