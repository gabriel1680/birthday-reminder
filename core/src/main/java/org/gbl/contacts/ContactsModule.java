package org.gbl.contacts;

import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.add.AddContactOutput;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;

public interface ContactsModule {
    AddContactOutput addContact(AddContactInput input);

    void updateContact(UpdateContactInput input);

    void removeContact(RemoveContactInput input);

    ContactOutput getContact(GetContactInput input);

    PaginationOutput<ContactOutput> listContacts(SearchInput<ContactFilter> input);
}
