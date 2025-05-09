package org.gbl.controller.contacts;

import org.gbl.HttpAPIResponse;
import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.service.query.InvalidSearchInputException;
import org.gbl.contacts.application.usecase.add.ContactAlreadyExistsException;
import org.gbl.contacts.application.usecase.shared.ContactNotFoundException;
import spark.Request;
import spark.Response;

import java.util.function.Supplier;

import static org.eclipse.jetty.http.HttpStatus.Code.BAD_REQUEST;
import static org.eclipse.jetty.http.HttpStatus.Code.NOT_FOUND;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;

public class ContactsAPIProxy extends BasicContactsAPI implements ContactsAPI {

    public ContactsAPIProxy(ContactsModule contactsModule) {
        super(contactsModule);
    }

    @Override
    public HttpAPIResponse createContact(Request request, Response response) {
        return wrap(request, response, () -> super.createContact(request, response));
    }

    @Override
    public HttpAPIResponse getContract(Request request, Response response) {
        return wrap(request, response, () -> super.getContract(request, response));
    }

    @Override
    public HttpAPIResponse deleteContact(Request request, Response response) {
        return wrap(request, response, () -> super.deleteContact(request, response));
    }

    @Override
    public HttpAPIResponse updateContact(Request request, Response response) {
        return wrap(request, response, () -> super.updateContact(request, response));
    }

    @Override
    public HttpAPIResponse searchContacts(Request request, Response response) {
        return wrap(request, response, () -> super.searchContacts(request, response));
    }

    private HttpAPIResponse wrap(Request request, Response response,
                                 Supplier<HttpAPIResponse> supplier) {
        response.type("application/json");
        try {
            return supplier.get();
        } catch (ContactAlreadyExistsException e) {
            response.status(UNPROCESSABLE_ENTITY.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        } catch (InvalidPayloadException | InvalidSearchInputException |
                 IllegalArgumentException e) {
            response.status(BAD_REQUEST.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        } catch (ContactNotFoundException e) {
            response.status(NOT_FOUND.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        }
    }
}
