package org.gbl;

import org.gbl.common.search.Pagination;
import org.gbl.out.ContactResponse;

public record SearchPageData(Pagination<ContactResponse> pagination) {
}
