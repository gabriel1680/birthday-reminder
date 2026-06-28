package org.gbl;

import org.gbl.common.search.Pagination;
import org.gbl.common.gateway.ContactResponse;

public record SearchPageData(Pagination<ContactResponse> pagination) {
}
