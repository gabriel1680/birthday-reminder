package org.gbl.contacts;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.infra.UUIDIdProvider;
import org.gbl.contacts.infra.InMemoryContactRepository;
import org.gbl.contacts.application.usecase.add.AddContact;
import org.gbl.contacts.application.usecase.get.GetContact;
import org.gbl.contacts.infra.InMemoryContactQueryRepository;
import org.gbl.contacts.application.usecase.list.ListContacts;
import org.gbl.contacts.application.usecase.remove.RemoveContact;
import org.gbl.contacts.application.usecase.update.UpdateContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsModuleFactory {

    public static ContactsModule createInMemory() {
        final var contacts = new ArrayList<Contact>();
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
