package org.gbl.common.search;

public record SearchRequest<T>(int page, int size, SortingOrder order, T filter) {
}
