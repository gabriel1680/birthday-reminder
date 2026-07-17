package org.gbl.controller.contacts;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.controller.common.HttpAPIResponse;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static org.eclipse.jetty.http.HttpStatus.Code.NO_CONTENT;
import static org.eclipse.jetty.http.HttpStatus.Code.OK;

public class ContactsAPIImpl implements ContactsAPI {

    private final ContactsModule contactsModule;
    private final ContactsJSONPresenter presenter;
    private final ContactsRequestParser requestParser;

    public ContactsAPIImpl(ContactsModule contactsModule) {
        this.contactsModule = contactsModule;
        this.presenter = new ContactsJSONPresenter();
        this.requestParser = new ContactsRequestParser();
    }

    @Override
    public HttpAPIResponse createContact(Request request, Response response) {
        final var output = contactsModule.addContact(requestParser.parseBody(request));
        response.status(CREATED.getCode());
        return HttpAPIResponse.ofSuccess(presenter.toJson(output));
    }

    @Override
    public HttpAPIResponse getContact(Request request, Response response) {
        final var input = new GetContactInput(requestParser.getId(request));
        final var contact = contactsModule.getContact(input);
        response.status(OK.getCode());
        return HttpAPIResponse.ofSuccess(presenter.toJson(contact));
    }

    @Override
    public HttpAPIResponse deleteContact(Request request, Response response) {
        final var input = new RemoveContactInput(requestParser.getId(request));
        contactsModule.removeContact(input);
        response.status(NO_CONTENT.getCode());
        return HttpAPIResponse.empty();
    }

    @Override
    public HttpAPIResponse updateContact(Request request, Response response) {
        final var id = requestParser.getId(request);
        contactsModule.updateContact(requestParser.parseBody(id, request));
        response.status(NO_CONTENT.getCode());
        return HttpAPIResponse.empty();
    }

    @Override
    public HttpAPIResponse searchContacts(Request request, Response response) {
        final var output = contactsModule.search(requestParser.parseSearchInput(request));
        response.status(OK.getCode());
        return HttpAPIResponse.ofSuccess(presenter.toJson(output));
    }

    @Override
    public HttpAPIResponse upcomingBirthdays(Request request, Response response) {
        final var input = requestParser.parseUpcomingBirthdays(request);
        final var output = contactsModule.upcomingBirthdays(input);
        response.status(OK.getCode());
        return HttpAPIResponse.ofSuccess(presenter.toJson(output));
    }
}
