package org.gbl.contacts.application.usecase.update;

import java.time.LocalDate;

public record UpdateContactInput(String id, String name, LocalDate birthdate) {
}
