package org.gbl.shared;

public record SearchInput<F>(int page, int take, int offset, SortingOrder order, F filter) {
}
