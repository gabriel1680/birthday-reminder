package org.gbl.shared;

public record SearchRequest<F>(int page, int take, int offset, SortingOrder order, F filter) {
}
