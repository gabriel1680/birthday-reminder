package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.CreateContactRequest;
import org.gbl.view.contacts.CreateContactViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class CreateContactController {

    private final ContactsGateway gateway;

    public CreateContactController(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    public void createPage(Context context) {
        render(context, CreateContactViewModel.empty());
    }

    public void createContact(Context context) {
        final var name = valueOrEmpty(context.formParam("name")).trim();
        final var birthdateValue = valueOrEmpty(context.formParam("birthdate"));
        final var nameError = name.isBlank() ? "Enter a contact name." : null;
        String birthdateError = null;
        LocalDate birthdate = null;

        if (birthdateValue.isBlank()) {
            birthdateError = "Enter a birthday.";
        } else {
            try {
                birthdate = LocalDate.parse(birthdateValue);
            } catch (DateTimeParseException exception) {
                birthdateError = "Enter a valid birthday.";
            }
        }

        final var viewModel = new CreateContactViewModel(
                name, birthdateValue, nameError, birthdateError);
        if (viewModel.hasErrors()) {
            context.status(400);
            render(context, viewModel);
            return;
        }

        final var contact = gateway.create(new CreateContactRequest(name, birthdate));
        context.redirect("/contacts/" + contact.id());
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private static void render(Context context, CreateContactViewModel viewModel) {
        context.render("contacts/create-page.jte", Map.of("viewModel", viewModel));
    }
}
