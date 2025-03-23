package org.gbl;

import picocli.CommandLine.Command;

@Command(name = "breminder",
        mixinStandardHelpOptions = true,
        version = "Birthday Reminder 1.0",
        description = "Manages contacts and birthday reminders",
        footerHeading = "Copyright%n", footer = "(c) Copyright by the authors",
        subcommands = {
                CreateContact.class,
                GetContact.class,
                DeleteContact.class,
                UpdateContact.class,
        })
public class BReminder implements Runnable {

    @Override
    public void run() {
    }
}
