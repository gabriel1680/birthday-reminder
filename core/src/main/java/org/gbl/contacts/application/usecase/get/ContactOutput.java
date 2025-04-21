package org.gbl.contacts.application.usecase.get;

import java.time.LocalDate;

public record ContactOutput(String id, String name, LocalDate birthdate) {
}
