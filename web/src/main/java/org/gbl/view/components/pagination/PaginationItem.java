package org.gbl.view.components.pagination;

public record PaginationItem(
        int page,
        boolean current,
        boolean ellipsis
) {}