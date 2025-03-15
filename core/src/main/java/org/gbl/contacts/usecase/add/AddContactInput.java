package org.gbl.contacts.usecase.add;

import java.time.LocalDate;

public record AddContactInput(String name, LocalDate birthdate) {
}
