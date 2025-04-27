package org.gbl.in;

import jakarta.inject.Inject;
import org.gbl.out.ContactsGateway;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "delete",
        mixinStandardHelpOptions = true,
        description = "Delete an existent contact by id")
public class DeleteContact implements Callable<Integer> {

    private final ContactsGateway gateway;

    @Parameters(index = "0", description = "The contact id to delete.")
    private String id;

    @Inject
    public DeleteContact(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Integer call() {
        final var response = gateway.delete(id)
                .onSuccess(it -> {
                    System.out.printf("Contact of id %s deleted%n", id);
                }).onFailure(error -> {
                    System.err.println(error.getMessage());
                });
        return response.isFailure() ? 1 : 0;
    }
}
