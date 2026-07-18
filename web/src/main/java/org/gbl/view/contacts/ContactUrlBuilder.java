package org.gbl.view.contacts;

import org.gbl.common.search.ContactFilter;

public class ContactUrlBuilder {

    private final String baseUrl;

    public ContactUrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String page(int page, ContactFilter filter) {
        return baseUrl + "?" + query(page, filter);
    }

    public String next(int page, ContactFilter filter) {
        return page(page + 1, filter);
    }

    public String previous(int page, ContactFilter filter) {
        return page(page - 1, filter);
    }

    private String query(int page, ContactFilter f) {
        final var sb = new StringBuilder();
        sb.append("page=").append(page);
        if (f.name() != null && !f.name().isBlank())
            sb.append("&name=").append(f.name());
        if (f.birthdateFrom() != null)
            sb.append("&birthdateFrom=").append(f.birthdateFrom());
        if (f.birthdateTo() != null)
            sb.append("&birthdateTo=").append(f.birthdateTo());
        return sb.toString();
    }
}