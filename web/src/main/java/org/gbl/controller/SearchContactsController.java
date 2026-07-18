package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.gbl.view.contacts.ContactSearchPresenter;
import org.gbl.view.contacts.SearchViewModel;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class SearchContactsController {

    private final ContactsGateway contactsGateway;
    private final ContactSearchPresenter presenter;
    public SearchContactsController(ContactsGateway contactsGateway, ContactSearchPresenter presenter) {
        this.contactsGateway = contactsGateway;
        this.presenter = presenter;
    }

    public void searchPage(Context context) {
        final var request = createSearchRequestFrom(context);
        final var pagination = contactsGateway.search(request).get();
        final var viewModel = presenter.toView(pagination, request.filter());
        renderSearchPage(context, viewModel);
    }

    private void renderSearchPage(Context context, SearchViewModel viewModel) {
        context.header("Cache-Control", "public, max-age=30, must-revalidate");
        context.render("contacts/search-page.jte", Map.of("viewModel", viewModel));
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
