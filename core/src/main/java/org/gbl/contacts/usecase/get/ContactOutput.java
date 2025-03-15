package org.gbl.contacts.usecase.get;

import java.time.LocalDate;

public record ContactOutput(String id, String name, LocalDate birthdate) {
}
