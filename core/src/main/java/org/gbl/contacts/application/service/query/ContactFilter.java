package org.gbl.contacts.application.service.query;

import java.time.LocalDate;

public record ContactFilter(String name, BirthdateFilter birthdateFilter) {

    public static ContactFilter of() {
        return new ContactFilter(null, null);
    }

    public static ContactFilter of(String name) {
        return new ContactFilter(name, null);
    }

    public static ContactFilter of(LocalDate birthdateFrom, LocalDate birthdateTo) {
        return new ContactFilter(null, new BirthdateFilter(birthdateFrom, birthdateTo));
    }

    public static ContactFilter of(String name, LocalDate birthdateFrom, LocalDate birthdateTo) {
        return new ContactFilter(name, new BirthdateFilter(birthdateFrom, birthdateTo));
    }
}
