package org.gbl.contacts.application.usecase.add;

import java.time.LocalDate;

public record AddContactInput(String name, LocalDate birthdate) {
}
