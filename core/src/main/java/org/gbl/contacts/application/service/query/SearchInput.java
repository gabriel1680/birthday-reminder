package org.gbl.contacts.application.service.query;

public record SearchInput<F>(int page, int take, int offset, SortingOrder order, F filter) {
}
