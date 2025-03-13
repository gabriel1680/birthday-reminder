package org.gbl.contacts.usecase;

import org.gbl.contacts.domain.Contact;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

public class ContactFixture {

    public static final Contact JOHN_DOE = new Contact("John Doe", "119991234", toDate("19/09/1999"));

    public static LocalDate toDate(String birthdate) {
        return LocalDate.parse(birthdate, ofPattern("dd/MM/yyyy"));
    }
}
