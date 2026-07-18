package org.gbl.view.common;

import java.util.Collection;

public record PaginationView<T>(PaginationWindow window, int total, Collection<T> values) {}
