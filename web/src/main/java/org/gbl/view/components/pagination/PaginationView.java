package org.gbl.view.components.pagination;

import java.util.Collection;

public record PaginationView<T>(PaginationWindow window, int total, Collection<T> values) {}
