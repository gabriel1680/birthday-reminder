package org.gbl.out;

public record SearchRequest<T>(int page, int size, SortingOrder order, T filter) {
}
