package org.gbl.in;

import jakarta.inject.Inject;
import org.gbl.out.ContactsGateway;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "update",
        optionListHeading = "Help & Version\n",
        mixinStandardHelpOptions = true,
        description = "Update an existent contact")
public class UpdateContact implements Callable<Integer> {

    private final ContactsGateway gateway;

    @Parameters(index = "0", description = "The contact id to update.")
    private String id;

    @ArgGroup(exclusive = false, heading = "Optional flags\n")
    private UpdateContactRequest request;

    @Inject
    public UpdateContact(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    public static class UpdateContactRequest {
        public String id;

        @Option(names = {"-n", "--name"},
                description = "The name of the contact.")
        public String name;

        @Option(names = {"-b", "--birthdate"},
                description = "The birthdate of the contact in ISO format (Eg.: " +
                        "2018-11-15T00:00:00Z).")
        public String birthdate;

        public UpdateContactRequest() {
        }

        public UpdateContactRequest(String name, String birthdate) {
            this.name = name;
            this.birthdate = birthdate;
        }
    }

    @Override
    public Integer call() {
        if (request == null) {
            var errMsg = "Error: Missing one of arguments: --name=<name> --birthdate=<birthdate>";
            System.err.print(errMsg);
            return 2;
        }
        request.id = id;
        final var response = gateway.update(request)
                .onSuccess(v -> {
                    System.out.println("Contact updated ✅");
                }).onFailure(error -> {
                    System.err.println(error.getMessage());
                });
        return response.isFailure() ? 1 : 0;
    }
}
