package org.gbl.controller.contacts;

import org.gbl.contacts.application.service.query.ContactFilter;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.upcoming_birthdays.GetUpcomingBirthdaysInput;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;
import org.gbl.controller.common.InvalidPayloadException;
import org.gbl.controller.common.RequestParser;
import org.json.JSONObject;
import spark.Request;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ContactsRequestParser extends RequestParser {

    public UpdateContactInput parseBody(String id, Request request) {
        try {
            final var json = new JSONObject(request.body());
            final var birthdate = parseBirthdate(json.getString("birthdate"));
            final var name = json.getString("name");
            return new UpdateContactInput(id, name, birthdate);
        } catch (RuntimeException e) {
            throw new InvalidPayloadException(e);
        }
    }

    public AddContactInput parseBody(Request request) {
        try {
            final var json = new JSONObject(request.body());
            final var birthdate = parseBirthdate(json.getString("birthdate"));
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

    private static LocalDate parseBirthdate(String value) {
        try {
            return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (RuntimeException exception) {
            return LocalDate.parse(value, DateTimeFormatter.ISO_DATE_TIME);
        }
    }

    public GetUpcomingBirthdaysInput parseUpcomingBirthdays(Request request) {
        final var clientZoneId = request.headers("X-Time-Zone");
        if (clientZoneId == null) {
            throw new InvalidPayloadException("X-Time-Zone header missing");
        }
        final var MAX_SIZE = 10;
        final var size = Integer.parseInt(request.queryParamOrDefault("size", "5"));
        return new GetUpcomingBirthdaysInput(Math.min(MAX_SIZE, size), ZoneId.of(clientZoneId));
    }
}
