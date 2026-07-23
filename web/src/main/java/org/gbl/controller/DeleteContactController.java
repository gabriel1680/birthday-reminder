package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactsGateway;

public class DeleteContactController {

    private final ContactsGateway gateway;

    public DeleteContactController(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    public void deleteContact(Context context) {
        gateway.delete(context.pathParam("id"));
        context.redirect("/contacts");
    }
}
