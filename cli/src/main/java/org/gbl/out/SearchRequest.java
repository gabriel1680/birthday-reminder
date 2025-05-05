package org.gbl.out;

public record SearchRequest(int page, int size, SortingOrder order, Object filter) {
}
