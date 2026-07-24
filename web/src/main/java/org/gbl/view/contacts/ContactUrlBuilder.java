package org.gbl.view.contacts;

import org.gbl.common.search.ContactFilter;
import org.gbl.view.common.UrlBuilder;

public class ContactUrlBuilder extends UrlBuilder<ContactFilter> {

    public ContactUrlBuilder(String baseUrl) {
        super(baseUrl);
    }

    protected String query(int page, ContactFilter f) {
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