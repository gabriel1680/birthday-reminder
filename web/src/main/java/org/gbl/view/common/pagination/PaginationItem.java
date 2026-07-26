package org.gbl.view.common.pagination;

public record PaginationItem(
        int page,
        boolean current,
        boolean ellipsis
) {}