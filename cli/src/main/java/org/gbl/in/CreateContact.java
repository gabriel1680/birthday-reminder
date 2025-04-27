package org.gbl.in;

import jakarta.inject.Inject;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "create",
        optionListHeading = "Help & Version\n",
        mixinStandardHelpOptions = true,
        description = "Create a new contact")
public class CreateContact implements Callable<Integer> {

    @ArgGroup(exclusive = false, heading = "Required flags\n")
    private CreateContactRequest request;

    private final ContactsGateway contactsGateway;

    @Inject
    public CreateContact(ContactsGateway contactsGateway) {
        this.contactsGateway = contactsGateway;
    }

    public static class CreateContactRequest {
        @Option(names = {"-n", "--name"}, required = true,
                description = "The name of the contact.")
        public String name;

        @Option(names = {"-b", "--birthdate"}, required = true,
                description = "The birthdate of the contact in ISO format (Eg.: " +
                        "2018-11-15T00:00:00Z).")
        public String birthdate;

        // needed in order to the framework to instantiate the class
        public CreateContactRequest() {
        }

        public CreateContactRequest(String name, String birthdate) {
            this.name = name;
            this.birthdate = birthdate;
        }
    }

    @Override
    public Integer call() {
        final var response = contactsGateway.create(request)
                .onSuccess(CreateContact::onSuccess)
                .onFailure(CreateContact::onFailure);
        return response.isFailure() ? 1 : 0;
    }

    private static void onFailure(Throwable error) {
        System.err.printf("Error: %s%n", error.getMessage());
    }

    private static void onSuccess(ContactResponse contact) {
        final var msg = "Create contact with id: %s, name: %s and birthdate: %s%n";
        System.out.printf(msg, contact.id(), contact.name(), contact.birthdate());
    }
}
