package org.gbl.contacts.usecase.get;

import java.time.LocalDate;

public record ContactOutput(String name, String number, LocalDate birthdate) {
}
