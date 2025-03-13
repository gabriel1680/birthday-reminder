package org.gbl.contacts.usecase.add;

import java.time.LocalDate;

public record AddContactRequest(String name, String number, LocalDate birthdate) {
}
