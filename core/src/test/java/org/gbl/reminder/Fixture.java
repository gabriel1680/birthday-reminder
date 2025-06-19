package org.gbl.reminder;

import org.gbl.contacts.application.usecase.get.ContactOutput;

import java.time.LocalDate;

public class Fixture {
    public static final ContactOutput IAN =
            new ContactOutput(
                    "8b5eb136-afe5-473d-a0c9-d259cdfd7fc9",
                    "Ian",
                    LocalDate.of(1990, 5, 22));

    public static final ContactOutput DONALD =
            new ContactOutput(
                    "8b5eb136-afe5-473d-a0c9-d259cdfd7fc1",
                    "Donald",
                    LocalDate.of(1975, 9, 24));
}
