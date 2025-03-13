package org.gbl;

import java.time.LocalDate;

public record AddContactRequest(String name, String number, LocalDate birthdate) {
}
