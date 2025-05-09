package org.gbl.controller.contacts;

import org.gbl.controller.HttpAPIResponse;
import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.add.AddContactOutput;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;
import org.gbl.controller.exceptions.InvalidPayloadException;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static org.eclipse.jetty.http.HttpStatus.Code.NO_CONTENT;
import static org.eclipse.jetty.http.HttpStatus.Code.OK;

class BasicContactsAPI implements ContactsAPI {

    private final ContactsModule contactsModule;

    public BasicContactsAPI(ContactsModule contactsModule) {
        this.contactsModule = contactsModule;
    }

    public HttpAPIResponse createContact(Request request, Response response) {
        final var output = contactsModule.addContact(parseBodyFrom(request));
        response.status(CREATED.getCode());
        return HttpAPIResponse.ofSuccess(toJson(output));
    }

    private static JSONObject toJson(AddContactOutput output) {
        return new JSONObject()
                .put("id", output.id())
                .put("name", output.name())
                .put("birthdate", output.birthdate());
    }

    private static AddContactInput parseBodyFrom(Request request) {
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

    public HttpAPIResponse getContract(Request request, Response response) {
        final var input = new GetContactInput(getId(request));
        final var contact = contactsModule.getContact(input);
        response.status(OK.getCode());
        return HttpAPIResponse.ofSuccess(toJson(contact));
    }

    private static String getId(Request request) {
        final var id = request.params("id");
        if (id == null || id.isEmpty())
            throw new InvalidPayloadException("invalid id");
        return id;
    }

    private static JSONObject toJson(ContactOutput contact) {
        return new JSONObject()
                .put("id", contact.id())
                .put("name", contact.name())
                .put("birthdate", contact.birthdate());
    }

    public HttpAPIResponse deleteContact(Request request, Response response) {
        final var id = getId(request);
        final var input = new RemoveContactInput(id);
        contactsModule.removeContact(input);
        response.status(NO_CONTENT.getCode());
        return HttpAPIResponse.empty();
    }

    public HttpAPIResponse updateContact(Request request, Response response) {
        contactsModule.updateContact(parseBodyFrom(getId(request), request));
        response.status(NO_CONTENT.getCode());
        return HttpAPIResponse.empty();
    }

    private static UpdateContactInput parseBodyFrom(String id, Request request) {
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

    public HttpAPIResponse searchContacts(Request request, Response response) {
        final var output = contactsModule.listContacts(inputOf(request));
        final var json = jsonFor(output);
        response.status(OK.getCode());
        return HttpAPIResponse.ofSuccess(json);
    }

    private static SearchInput<ContactFilter> inputOf(Request request) {
        final var page = Integer.parseInt(request.queryParamOrDefault("page", "1"));
        final var size = Integer.parseInt(request.queryParamOrDefault("size", "5"));
        final var order = SortingOrder.of(request.queryParamOrDefault("order", "asc"));
        final var filter = contactFilterOf(request);
        return new SearchInput<>(page, size, order, filter);
    }

    private static ContactFilter contactFilterOf(Request request) {
        final var nameFilter = request.queryParams("filter_name");
        var filter = ContactFilter.of(nameFilter);
        final var birthdateFilter = request.queryParams("filter_birthdate");
        if (birthdateFilter != null) {
            filter = ContactFilter.of(nameFilter, LocalDate.parse(birthdateFilter));
        }
        return filter;
    }

    private static JSONObject jsonFor(PaginationOutput<ContactOutput> output) {
        final var values = output.values().stream()
                .reduce(new JSONArray(),
                        (acc, next) -> acc.put(toJson(next)),
                        JSONArray::putAll);
        return new JSONObject()
                .put("current_page", output.page())
                .put("size", output.size())
                .put("total", output.total())
                .put("last_page", output.lastPage())
                .put("values", values);
    }
}
