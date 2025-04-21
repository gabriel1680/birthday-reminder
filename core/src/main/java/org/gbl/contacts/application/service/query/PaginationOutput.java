package org.gbl.contacts.application.service.query;

import java.util.List;

public record PaginationOutput<T>(int page, int take, int total, List<T> values) {
}
