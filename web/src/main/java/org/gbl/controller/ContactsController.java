package org.gbl.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class ContactsController extends JavalinController {

    private static final String DETAILS_PAGE = "contacts/details-page.jte";
    private static final String CREATE_PAGE = "contacts/create-page.jte";

    private final ContactsService contactsService;
    private final ContactsPresenter presenter;

    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
        presenter = new ContactsPresenter();
    }

    public void createPage(Context context) {
        final var viewModel = presenter.emptyCreateForm();
        context.render(CREATE_PAGE, toViewModelMap(viewModel));
    }

    public void createContact(Context context) {
        final var form = new CreateContactForm(
                context.formParam("name"), context.formParam("birthdate"));
        contactsService.createContact(form)
                .peekLeft(exception -> handleFormValidationError(context, exception))
                .peek(contact -> context.redirect("/contacts/" + contact.id()));
    }

    private void handleFormValidationError(Context context, InvalidContactFormException exception) {
        context.status(HttpStatus.BAD_REQUEST);
        final var validation = exception.validation();
        final var viewModel = presenter.createContactError(validation.name(),
                                                           validation.birthdate(),
                                                           validation.nameError(),
                                                           validation.birthdateError());
        context.render(CREATE_PAGE, toViewModelMap(viewModel));
    }

    public void deleteContact(Context context) {
        contactsService.delete(idFrom(context));
        context.redirect("/contacts");
    }

    public void contactInfo(Context context) {
        final var contactResponse = contactsService.getOf(idFrom(context));
        final var viewModel = presenter.toView(contactResponse);
        context.render(DETAILS_PAGE, toViewModelMap(viewModel));
    }
}
