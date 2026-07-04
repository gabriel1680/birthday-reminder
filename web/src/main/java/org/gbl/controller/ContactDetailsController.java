package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.view.ContactSearchPresenter;

import java.util.Map;

import static org.gbl.controller.ErrorController.internalServerErrorPage;

public class ContactDetailsController {

    private final ContactsGateway gateway;

    public ContactDetailsController(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    public void contactInfo(Context context) {
        gateway.get(context.pathParam("id"))
                .onSuccess(contactResponse -> renderDetailsPage(context, contactResponse))
                .onFailure(t -> internalServerErrorPage(context, t));
    }

    private static void renderDetailsPage(Context context, ContactResponse contactResponse) {
        final var viewMode = ContactSearchPresenter.toContactView(contactResponse);
        context.render("contacts/details-page.jte", Map.of("viewModel", viewMode));
    }
}
