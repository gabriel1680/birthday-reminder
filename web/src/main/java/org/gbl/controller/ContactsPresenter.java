package org.gbl.controller;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.view.contacts.ContactView;
import org.gbl.view.contacts.CreateContactViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ContactsPresenter {

    private static final DateTimeFormatter BIRTHDATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CreateContactViewModel emptyCreateForm() {
        return CreateContactViewModel.empty();
    }

    public CreateContactViewModel createContactError(String name, LocalDate birthdate,
                                                     String nameError, String birthdateError) {
        return new CreateContactViewModel(
                name,
                birthdate.toString(),
                nameError,
                birthdateError);
    }

    public ContactView toView(ContactResponse contact) {
        return new ContactView(
                contact.id(),
                contact.name(),
                BIRTHDATE_FORMAT.format(contact.birthdate()));
    }
}
