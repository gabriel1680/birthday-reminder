package org.gbl.controller.contacts;

import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.service.query.ContactFilter;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;
import org.gbl.controller.exceptions.InvalidPayloadException;
import org.json.JSONObject;
import spark.Request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ContactsRequestParser {

    public String getId(Request request) {
        final var id = request.params("id");
        if (id == null || id.isEmpty())
            throw new InvalidPayloadException("invalid id");
        return id;
    }

    public UpdateContactInput parseBody(String id, Request request) {
        try {
            final var json = new JSONObject(request.body());
            final var birthdate = LocalDate.parse(json.getString("birthdate"),
                                                  DateTimeFormatter.ISO_DATE_TIME);
            final var name = json.getString("name");
            return new UpdateContactInput(id, name, birthdate);
        } catch (RuntimeException e) {
            throw new InvalidPayloadException(e);
        }
    }

    public AddContactInput parseBody(Request request) {
        try {
            final var json = new JSONObject(request.body());
            final var birthdate = LocalDate.parse(json.getString("birthdate"),
                                                  DateTimeFormatter.ISO_DATE_TIME);
            final var name = json.getString("name");
            return new AddContactInput(name, birthdate);
        } catch (RuntimeException e) {
            throw new InvalidPayloadException(e);
        }
    }

    public SearchInput<ContactFilter> parseSearchInput(Request request) {
        final var page = Integer.parseInt(request.queryParamOrDefault("page", "1"));
        final var size = Integer.parseInt(request.queryParamOrDefault("size", "5"));
        final var order = SortingOrder.of(request.queryParamOrDefault("order", "asc"));
        final var filter = contactFilterOf(request);
        return new SearchInput<>(page, size, order, filter);
    }

    private static ContactFilter contactFilterOf(Request request) {
        final var nameFilter = request.queryParams("name");
        var filter = ContactFilter.of(nameFilter);
        final var birthdateFromFilter = request.queryParams("birthdateFrom");
        final var birthdateToFilter = request.queryParams("birthdateTo");
        if (birthdateFromFilter != null && birthdateToFilter != null) {
            filter = ContactFilter.of(nameFilter, toDate(birthdateFromFilter), toDate(birthdateToFilter));
        }
        return filter;
    }

    private static LocalDate toDate(String aDate) {
        return LocalDate.parse(aDate);
    }
}
