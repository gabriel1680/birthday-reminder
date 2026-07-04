package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.GetUpcomingBirthdaysRequest;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.gbl.view.ContactSearchPresenter;
import org.gbl.view.SearchViewModel;

import java.time.ZoneId;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.gbl.controller.ErrorController.internalServerErrorPage;

public class SearchContactsController {

    private final ContactsGateway contactsGateway;
    private final ContactSearchPresenter presenter;
    private final Executor executor;

    public SearchContactsController(ContactsGateway contactsGateway, ContactSearchPresenter presenter, Executor executor) {
        this.contactsGateway = contactsGateway;
        this.presenter = presenter;
        this.executor = executor;
    }

    public void searchPage(Context context) {
        final var request = createSearchRequestFrom(context);
        final var searchFuture = supplyAsync(() -> contactsGateway.search(request), executor);
        final var getUpcomingBirthdaysRequest = createUpcomingBirthdaysRequest(context);
        final var birthdaysFuture =
                supplyAsync(() -> contactsGateway.getUpcomingBirthdays(getUpcomingBirthdaysRequest), executor);
        final var combinedFuture =
                searchFuture.thenCombine(birthdaysFuture, (searchTry, upcomingBirthdaysTry) ->
                    searchTry.flatMap(pagination ->
                          upcomingBirthdaysTry.map(birthdays ->
                               presenter.toView(pagination, request.filter(), birthdays))));
        combinedFuture.join()
                .onSuccess(viewModel -> renderSearchPage(context, viewModel))
                .onFailure(err -> internalServerErrorPage(context, err));
    }

    private static GetUpcomingBirthdaysRequest createUpcomingBirthdaysRequest(Context context) {
        final var size = 3;
        final var clientZoneId = ZoneId.of("America/Sao_Paulo");
        return new GetUpcomingBirthdaysRequest(size, clientZoneId);
    }

    private void renderSearchPage(Context context, SearchViewModel viewModel) {
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
