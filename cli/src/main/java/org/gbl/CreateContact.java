package org.gbl;

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
    private Payload payload;

    static class Payload {
        @Option(names = {"-n", "--name"}, required = true,
                description = "The name of the contact.")
        private String name;
        @Option(names = {"-b", "--birthdate"}, required = true,
                description = "The birthdate of the contact in ISO format (Eg.: 2018-11-15T00:00:00Z).")
        private String birthdate;
    }

    @Override
    public Integer call() {
        final var msg = "Create contact called with name: %s and birthdate: %s";
        System.out.printf((msg) + "%n", payload.name, payload.birthdate);
        return 0;
    }
}
