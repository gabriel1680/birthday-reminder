package org.gbl.view.common.pagination;

import java.util.Collection;

public record PaginationView<T>(PaginationWindow window, int total, Collection<T> values) {}
