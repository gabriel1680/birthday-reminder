package org.gbl.app;

import jakarta.inject.Inject;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.time.format.DateTimeFormatter;
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
        try {
            onSuccess(gateway.get(id));
            return 0;
        } catch (RuntimeException error) {
            onFailure(error);
            return 1;
        }
    }

    private void onFailure(Throwable error) {
        System.err.printf("Error: %s%n", error.getMessage());
    }

    private void onSuccess(ContactResponse contact) {
        System.out.printf("Contact found => id: %s, name: %s, birthdate: %s%n",
                          contact.id(),
                          contact.name(),
                          contact.birthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
