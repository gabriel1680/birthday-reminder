package org.gbl.contacts.application.usecase.fixture;

import org.gbl.contacts.domain.Contact;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

public class ContactFixture {

    public static final Contact JOHN_DOE = new Contact("1", "John Doe", toDate("19/09/1999"));

    public static LocalDate toDate(String birthdate) {
        return LocalDate.parse(birthdate, ofPattern("dd/MM/yyyy"));
    }
}
