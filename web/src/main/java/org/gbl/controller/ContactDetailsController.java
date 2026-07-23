package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.view.contacts.ContactViewPresenter;

import java.util.Map;

public class ContactDetailsController {

    private final ContactsGateway gateway;

    public ContactDetailsController(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    public void contactInfo(Context context) {
        final var contactResponse = gateway.get(context.pathParam("id"));
        renderDetailsPage(context, contactResponse);
    }

    private static void renderDetailsPage(Context context, ContactResponse contactResponse) {
        final var viewModel = ContactViewPresenter.toView(contactResponse);
        context.render("contacts/details-page.jte", Map.of("viewModel", viewModel));
    }
}
