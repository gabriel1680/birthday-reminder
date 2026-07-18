package org.gbl.view.common;

public record PaginationItem(
        int page,
        boolean current,
        boolean ellipsis
) {}