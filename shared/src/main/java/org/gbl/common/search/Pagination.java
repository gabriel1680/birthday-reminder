package org.gbl.common.search;

import java.util.Collection;

import static java.util.Collections.emptyList;

public record Pagination<T>(int page, int size, int total, int lastPage, Collection<T> values) {

    public static <T> Pagination<T> empty() {
        return new Pagination<>(0, 0, 0, 0, emptyList());
    }
}
