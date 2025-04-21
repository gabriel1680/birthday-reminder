package org.gbl.contacts;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.infra.UUIDIdProvider;
import org.gbl.contacts.infra.InMemoryContactRepository;
import org.gbl.contacts.usecase.add.AddContact;
import org.gbl.contacts.usecase.get.GetContact;
import org.gbl.contacts.infra.InMemoryContactQueryRepository;
import org.gbl.contacts.usecase.list.ListContacts;
import org.gbl.contacts.usecase.remove.RemoveContact;
import org.gbl.contacts.usecase.update.UpdateContact;

import java.util.Collections;
import java.util.List;

public class ContactsModuleFactory {

    public static ContactsModule createInMemory() {
        final List<Contact> contacts = Collections.emptyList();
        final var repository = new InMemoryContactRepository(contacts);
        return new ContactsModuleFacade(
                new AddContact(repository, new UUIDIdProvider()),
                new UpdateContact(repository),
                new RemoveContact(repository),
                new GetContact(repository),
                new ListContacts(new InMemoryContactQueryRepository(contacts))
        );
    }
}
