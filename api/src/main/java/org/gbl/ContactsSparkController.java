package org.gbl;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.usecase.add.AddContactInput;
import org.gbl.contacts.usecase.add.ContactAlreadyExistsException;
import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.contacts.usecase.get.GetContactInput;
import org.gbl.contacts.usecase.remove.RemoveContactInput;
import org.gbl.contacts.usecase.shared.ContactNotFoundException;
import org.gbl.contacts.usecase.update.UpdateContactInput;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.eclipse.jetty.http.HttpStatus.Code.BAD_REQUEST;
import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static org.eclipse.jetty.http.HttpStatus.Code.NOT_FOUND;
import static org.eclipse.jetty.http.HttpStatus.Code.NO_CONTENT;
import static org.eclipse.jetty.http.HttpStatus.Code.OK;
import static org.eclipse.jetty.http.HttpStatus.Code.UNPROCESSABLE_ENTITY;

public class ContactsSparkController {

    private final ContactsModule contactsModule;

    public ContactsSparkController(ContactsModule contactsModule) {
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

    public HttpAPIResponse getContract(Request request, Response response) {
        response.type("application/json");
        try {
            final var input = new GetContactInput(getId(request));
            final var contact = contactsModule.getContact(input);
            response.status(OK.getCode());
            return HttpAPIResponse.ofSuccess(toJson(contact));
        } catch (InvalidPayloadException e) {
            response.status(BAD_REQUEST.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        } catch (ContactNotFoundException e) {
            response.status(NOT_FOUND.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        }
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
        response.type("application/json");
        try {
            final var id = getId(request);
            final var input = new RemoveContactInput(id);
            contactsModule.removeContact(input);
            response.status(NO_CONTENT.getCode());
            return HttpAPIResponse.empty();
        } catch (InvalidPayloadException e) {
            response.status(BAD_REQUEST.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        } catch (ContactNotFoundException e) {
            response.status(NOT_FOUND.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        }
    }

    public HttpAPIResponse updateContact(Request request, Response response) {
        response.type("application/json");
        try {
            final var id = getId(request);
            final var input = parseBodyFrom(id, request);
            contactsModule.updateContact(input);
            response.status(NO_CONTENT.getCode());
            return HttpAPIResponse.empty();
        } catch (InvalidPayloadException e) {
            response.status(BAD_REQUEST.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        } catch (ContactNotFoundException e) {
            response.status(NOT_FOUND.getCode());
            return HttpAPIResponse.ofError(e.getMessage());
        }
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
}
