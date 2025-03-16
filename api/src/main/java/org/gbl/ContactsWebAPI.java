package org.gbl;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.usecase.add.AddContactInput;
import org.gbl.contacts.usecase.add.ContactAlreadyExistsException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.eclipse.jetty.http.HttpStatus.Code.BAD_REQUEST;
import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;

public class ContactsWebAPI {

    private final ContactsModule contactsModule;

    public ContactsWebAPI(ContactsModule contactsModule) {
        this.contactsModule = contactsModule;
    }

    public HttpAPIResponse createContact(Request request, Response response) {
        response.type("application/json");
        try {
            contactsModule.addContact(parseBodyFrom(request));
            response.status(CREATED.getCode());
            return HttpAPIResponse.empty();
        } catch (ContactAlreadyExistsException e) {
            response.status(UNPROCESSABLE_ENTITY.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        } catch (InvalidPayloadException e) {
            response.status(BAD_REQUEST.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        }
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
}
