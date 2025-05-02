package org.gbl.contacts.application.usecase.list;

import java.time.LocalDate;

public record ContactFilter(String name, LocalDate birthdate) {

    public static ContactFilter of() {
        return new ContactFilter(null, null);
    }

    public static ContactFilter of(String name) {
        return new ContactFilter(name, null);
    }

    public static ContactFilter of(LocalDate birthdate) {
        return new ContactFilter(null, birthdate);
    }

    public static ContactFilter of(String name, LocalDate birthdate) {
        return new ContactFilter(name, birthdate);
    }
}
