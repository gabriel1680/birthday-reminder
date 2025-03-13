package org.gbl.shared;

import java.util.List;

public record PaginationOutput<T>(int page, int take, int total, List<T> values) {
}
