package org.gbl.contacts.application.service.query;

import java.time.LocalDate;

public record BirthdateFilter(LocalDate from, LocalDate to) {

    public boolean contains(LocalDate aBirthdate) {
        return !from.isBefore(aBirthdate) && !to.isAfter(aBirthdate);
    }

    public boolean isInValid() {
        return (from == null || to == null) || to.isAfter(from);
    }
}
