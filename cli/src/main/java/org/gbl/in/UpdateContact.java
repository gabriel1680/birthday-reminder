package org.gbl.in;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "update",
        optionListHeading = "Help & Version\n",
        mixinStandardHelpOptions = true,
        description = "Update an existent contact")
public class UpdateContact implements Callable<Integer> {

    @ArgGroup(exclusive = false, heading = "Optional flags\n")
    private Payload payload;

    static class Payload {
        @Option(names = {"-n", "--name"},
                description = "The name of the contact.")
        private String name;
        @Option(names = {"-b", "--birthdate"},
                description = "The birthdate of the contact in ISO format (Eg.: " +
                        "2018-11-15T00:00:00Z).")
        private String birthdate;
    }

    @Override
    public Integer call() {
        final var msg = "Update contact called with name: %s and birthdate: %s";
        System.out.printf((msg) + "%n", payload.name, payload.birthdate);
        return 0;
    }
}
