package org.gbl.contacts.application.service.query;

public record SearchInput<F>(int page, int size, SortingOrder order, F filter) {

    public SearchInput {
        if (page < 1) {
            throw new InvalidSearchInputException("invalid page: value should be greater than 0");
        } else if (size < 1) {
            throw new InvalidSearchInputException("invalid size: value should be greater than 0");
        }
    }

    public static SearchInput<Void> of(int page, int perPage) {
        return new SearchInput<>(page, perPage, SortingOrder.ASC, null);
    }

    public boolean hasFilter() {
        return filter != null;
    }

    public Offset offset() {
        final var start = (page - 1) * size;
        final var end = start + size;
        return new Offset(start, end);
    }

    public record Offset(int start, int end) {
    }
}
