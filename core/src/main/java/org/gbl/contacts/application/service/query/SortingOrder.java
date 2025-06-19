package org.gbl.contacts.application.service.query;

public enum SortingOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortingOrder(String value) {
        this.value = value;
    }

    public static SortingOrder of(String s) {
        for (var sortingOrder : values()) {
            if (sortingOrder.value().equals(s)) {
                return sortingOrder;
            }
        }
        throw new IllegalArgumentException("Invalid SortingOrder string value \"%s\"".formatted(s));
    }

    public String value() {
        return value;
    }
}
