package org.gbl.view.common;

import java.util.List;

public record PaginationWindow(
        List<PaginationItem> items,
        int currentPage,
        int lastPage,
        Integer previousPage,
        Integer nextPage,
        boolean hasPrevious,
        boolean hasNext) {
}