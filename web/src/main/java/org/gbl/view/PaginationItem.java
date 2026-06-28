package org.gbl.view;

public record PaginationItem(
        int page,
        boolean current,
        boolean ellipsis
) {}