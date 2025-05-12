package org.gbl.common.search;

import java.util.Collection;

public record Pagination<T>(int page, int size, int total, int lastPage, Collection<T> values) {
}
