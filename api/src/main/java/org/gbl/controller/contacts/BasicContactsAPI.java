package org.gbl.controller.contacts;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.controller.HttpAPIResponse;
import spark.Request;
import spark.Response;

import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static org.eclipse.jetty.http.HttpStatus.Code.NO_CONTENT;
import static org.eclipse.jetty.http.HttpStatus.Code.OK;

class BasicContactsAPI implements ContactsAPI {

    private final ContactsModule contactsModule;
    private final ContactsJSONMapper jsonMapper;
    private final ContactsRequestParser requestParser;

    public BasicContactsAPI(ContactsModule contactsModule) {
        this.contactsModule = contactsModule;
        this.jsonMapper = new ContactsJSONMapper();
        this.requestParser = new ContactsRequestParser();
    }

    public HttpAPIResponse createContact(Request request, Response response) {
        final var output = contactsModule.addContact(requestParser.parseBody(request));
        response.status(CREATED.getCode());
        return HttpAPIResponse.ofSuccess(jsonMapper.toJson(output));
    }

    public HttpAPIResponse getContract(Request request, Response response) {
        final var input = new GetContactInput(requestParser.getId(request));
        final var contact = contactsModule.getContact(input);
        response.status(OK.getCode());
        return HttpAPIResponse.ofSuccess(jsonMapper.toJson(contact));
    }

    public HttpAPIResponse deleteContact(Request request, Response response) {
        final var input = new RemoveContactInput(requestParser.getId(request));
        contactsModule.removeContact(input);
        response.status(NO_CONTENT.getCode());
        return HttpAPIResponse.empty();
    }

    public HttpAPIResponse updateContact(Request request, Response response) {
        final var id = requestParser.getId(request);
        contactsModule.updateContact(requestParser.parseBody(id, request));
        response.status(NO_CONTENT.getCode());
        return HttpAPIResponse.empty();
    }

    public HttpAPIResponse searchContacts(Request request, Response response) {
        final var output = contactsModule.listContacts(requestParser.parseSearchInput(request));
        response.status(OK.getCode());
        return HttpAPIResponse.ofSuccess(jsonMapper.toJson(output));
    }
}
