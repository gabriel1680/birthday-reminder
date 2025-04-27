package org.gbl.in;

import jakarta.inject.Inject;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "get", mixinStandardHelpOptions = true, description = "Get a contact by id")
public class GetContact implements Callable<Integer> {

    private final ContactsGateway gateway;

    @Parameters(index = "0", description = "The contact id to retrieve.")
    private String id;

    @Inject
    public GetContact(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Integer call() {
        final var response = gateway.get(id)
                .onSuccess(this::onSuccess)
                .onFailure(this::onFailure);
        return response.isFailure() ? 1 : 0;
    }

    private void onFailure(Throwable error) {
        System.out.printf("Error on get contact: %s%n", error.getMessage());
    }

    private void onSuccess(ContactResponse contact) {
        System.out.printf("Contact => id: %s, name: %s, birthdate: %s%n", contact.id(),
                          contact.name(), contact.birthdate());
    }
}
