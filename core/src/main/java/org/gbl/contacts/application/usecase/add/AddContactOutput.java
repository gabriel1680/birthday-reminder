package org.gbl.contacts.application.usecase.add;

import java.time.LocalDate;

public record AddContactOutput(String id, String name, LocalDate birthdate) {
}
