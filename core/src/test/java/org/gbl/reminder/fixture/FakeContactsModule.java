package org.gbl.reminder.fixture;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;

import java.util.ArrayList;
import java.util.List;

import static org.gbl.contacts.usecase.fixture.ContactFixture.JOHN_DOE;

public class FakeContactsModule implements ContactsModule {
    @Override
    public void addContact(AddContactInput input) {
    }

    @Override
    public void updateContact(UpdateContactInput input) {
    }

    @Override
    public void removeContact(RemoveContactInput input) {
    }

    @Override
    public ContactOutput getContact(GetContactInput input) {
        return null;
    }

    @Override
    public PaginationOutput<ContactOutput> listContacts(SearchInput<ContactFilter> input) {
        final List<ContactOutput> contacts = new ArrayList<>();
        if (input.filter().birthdate().atStartOfDay().getDayOfMonth() == 22) {
            final var contact = new ContactOutput(JOHN_DOE.id(),
                                                  JOHN_DOE.name(),
                                                  JOHN_DOE.birthdate());
            contacts.add(contact);
        }
        if (input.filter().birthdate().atStartOfDay().getDayOfMonth() == 24) {
            final var contact1 = new ContactOutput(JOHN_DOE.id(),
                                                   JOHN_DOE.name(),
                                                   JOHN_DOE.birthdate());
            final var contact2 = new ContactOutput(JOHN_DOE.id(),
                                                   JOHN_DOE.name(),
                                                   JOHN_DOE.birthdate());
            contacts.addAll(List.of(contact1, contact2));
        }
        return new PaginationOutput<>(input.page(), input.take(), contacts.size(), contacts);
    }
}
