package org.gbl;

import io.javalin.http.Context;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;

import java.util.Map;
import java.util.Optional;

import static java.lang.Integer.parseInt;

public class ContactsController {

    private final ContactsGateway contactsGateway;

    public ContactsController(ContactsGateway contactsGateway) {
        this.contactsGateway = contactsGateway;
    }

    public void searchPage(Context context) {
        final var request = createSearchRequestFrom(context);
        contactsGateway.search(request)
                .onSuccess(pagination -> createSearchPage(context, pagination))
                .onFailure(error -> internalServerErrorPage(context));
    }

    private static void internalServerErrorPage(Context context) {
        context.status(500);
        context.render("internal-server-error.jte");
    }

    private static void createSearchPage(Context context, Pagination<ContactResponse> pagination) {
        context.render("contacts.jte", Map.of("data", new SearchPageData(pagination)));
    }

    private static SearchRequest<ContactFilter> createSearchRequestFrom(Context context) {
        final var page = parseInt(queryParamOrDefault(context, "page", "1"));
        final var size = parseInt(queryParamOrDefault(context, "size", "1"));
        final var order = SortingOrder.of(queryParamOrDefault(context, "order", "asc"));
        return new SearchRequest<>(page, size, order, null);
    }

    private static String queryParamOrDefault(Context context,
                                              String queryParam,
                                              String defaultValue) {
        final var paramOrNull = context.queryParam(queryParam);
        return Optional.ofNullable(paramOrNull).orElse(defaultValue);
    }
}
