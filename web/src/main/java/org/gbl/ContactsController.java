package org.gbl;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.gbl.view.ContactSearchPresenter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class ContactsController {

    private final ContactsGateway contactsGateway;
    private final ContactSearchPresenter presenter;

    public ContactsController(ContactsGateway contactsGateway, ContactSearchPresenter presenter) {
        this.contactsGateway = contactsGateway;
        this.presenter = presenter;
    }

    public void searchPage(Context context) {
        final var request = createSearchRequestFrom(context);
        contactsGateway.search(request)
                .onSuccess(pagination -> createSearchPage(context, pagination, request.filter()))
                .onFailure(error -> internalServerErrorPage(context));
    }

    private static void internalServerErrorPage(Context context) {
        context.status(500);
        context.render("internal-server-error.jte");
    }

    private void createSearchPage(Context context, Pagination<ContactResponse> pagination,
                                  ContactFilter filter) {
        final var viewModel = presenter.toView(pagination, filter, pagination.values().stream().toList());
        context.render("contacts/search.jte", Map.of("viewModel", viewModel));
    }

    private static SearchRequest<ContactFilter> createSearchRequestFrom(Context context) {
        final var page = parseInt(queryParamOrDefault(context, "page", "1"));
        final var size = parseInt(queryParamOrDefault(context, "size", "15"));
        final var order = SortingOrder.of(queryParamOrDefault(context, "order", "asc"));
        final var filter = createFilterFrom(context);
        return new SearchRequest<>(page, size, order, filter);
    }

    private static String queryParamOrDefault(Context context,
                                              String queryParam,
                                              String defaultValue) {
        final var paramOrNull = context.queryParam(queryParam);
        return Optional.ofNullable(paramOrNull).orElse(defaultValue);
    }

    private static ContactFilter createFilterFrom(Context context) {
        final var name = context.queryParam("name");
        final var birthdateFrom = context.queryParam("birthdateFrom");
        final var birthdateTo = context.queryParam("birthdateTo");
        final var allNull = Stream.of(name, birthdateFrom, birthdateTo)
                .allMatch(Objects::isNull);
        if (allNull) {
            return ContactFilter.empty();
        }
        return new ContactFilter(name, birthdateFrom, birthdateTo);
    }
}
