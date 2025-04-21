package org.gbl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.add.ContactAlreadyExistsException;

public class ContactsController {
    private final ObjectMapper objectMapper;
    private final ContactsModule contactsModule;

    public ContactsController(ContactsModule contactsModule) {
        this.contactsModule = contactsModule;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void create(Context context) throws JsonProcessingException {
        context.contentType(ContentType.APPLICATION_JSON);
        try {
            final var request = objectMapper
                    .readValue(context.body(), CreateContactRequest.class);
            final var input = new AddContactInput(request.name(), request.birthdate());
            contactsModule.addContact(input);
            context.status(HttpStatus.CREATED);
        } catch (ContactAlreadyExistsException e) {
            context.status(HttpStatus.UNPROCESSABLE_CONTENT);
            context.result(e.getMessage());
        }
    }
}
