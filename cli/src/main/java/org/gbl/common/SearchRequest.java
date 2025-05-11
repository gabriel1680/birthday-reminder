package org.gbl.common;

public record SearchRequest<T>(int page, int size, SortingOrder order, T filter) {
}
