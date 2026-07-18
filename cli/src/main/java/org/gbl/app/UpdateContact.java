package org.gbl.app;

import jakarta.inject.Inject;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.UpdateContactRequest;
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
    private UpdateContactData data;

    @Inject
    public UpdateContact(ContactsGateway gateway) {
        this.gateway = gateway;
    }

    public static class UpdateContactData {
        @Option(names = {"-n", "--name"},
                description = "The name of the contact.")
        public String name;

        @Option(names = {"-b", "--birthdate"},
                description = "The birthdate of the contact in ISO format (Eg.: " +
                        "2018-11-15T00:00:00Z).")
        public String birthdate;
    }

    @Override
    public Integer call() {
        if (data == null) {
            var errMsg = "Error: Missing one of arguments: --name=<name> --birthdate=<birthdate>";
            System.err.print(errMsg);
            return 2;
        }
        final var request = new UpdateContactRequest(id, data.name, data.birthdate);
        try {
            gateway.update(request);
            onSuccess(null);
            return 0;
        } catch (RuntimeException error) {
            onError(error);
            return 1;
        }
    }

    private static void onSuccess(Void v) {
        System.out.println("Contact updated ✅");
    }

    private static void onError(Throwable error) {
        System.err.printf("Error: %s%n", error.getMessage());
    }
}
