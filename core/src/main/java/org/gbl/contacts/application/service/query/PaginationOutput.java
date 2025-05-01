package org.gbl.contacts.application.service.query;

import java.util.Collections;
import java.util.List;

public record PaginationOutput<T>(int page, int size, int total, List<T> values) {

    public static <T> PaginationOutput<T> emptyOf(int page, int take) {
        return new PaginationOutput<>(page, take, 0, Collections.emptyList());
    }
}
