package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.CreateContactRequest;
import org.gbl.view.contacts.CreateContactViewModel;

import java.util.Map;

public class CreateContactController {

    private final ContactsGateway gateway;
    private final CreateContactValidator validator;

    public CreateContactController(ContactsGateway gateway) {
        this.gateway = gateway;
        this.validator = new CreateContactValidator();
    }

    public void createPage(Context context) {
        render(context, CreateContactViewModel.empty());
    }

    public void createContact(Context context) {
        final var form = createContactForm(context);
        final var validation = validator.validate(form);
        if (validation.hasErrors()) {
            context.status(400);
            render(context, createContactViewModel(form, validation));
            return;
        }
        final var request = createContactRequest(validation);
        final var contact = gateway.create(request);
        context.redirect("/contacts/" + contact.id());
    }

    private static CreateContactRequest createContactRequest(CreateContactValidation validation) {
        return new CreateContactRequest(
                validation.name(), validation.birthdate());
    }

    private static CreateContactForm createContactForm(Context context) {
        return new CreateContactForm(
                context.formParam("name"), context.formParam("birthdate"));
    }

    private static CreateContactViewModel createContactViewModel(CreateContactForm form, CreateContactValidation validation) {
        return new CreateContactViewModel(
                form.name(),
                form.birthdate(),
                validation.nameError(),
                validation.birthdateError());
    }

    private static void render(Context context, CreateContactViewModel viewModel) {
        context.render("contacts/create-page.jte", Map.of("viewModel", viewModel));
    }
}
