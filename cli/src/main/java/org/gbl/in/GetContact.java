package org.gbl.in;

import jakarta.inject.Inject;
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
        var contact = gateway.get(id);
        System.out.printf("Contact => id: %s, name: %s, birthdate: %s", contact.id(), contact.name(), contact.birthdate());
        return 0;
    }
}
