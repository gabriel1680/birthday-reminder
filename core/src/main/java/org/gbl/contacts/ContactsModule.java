package org.gbl.contacts;

import org.gbl.contacts.usecase.add.AddContactInput;
import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.contacts.usecase.get.GetContactInput;
import org.gbl.contacts.usecase.list.ContactFilter;
import org.gbl.contacts.usecase.remove.RemoveContactInput;
import org.gbl.shared.PaginationOutput;
import org.gbl.shared.SearchInput;

public interface ContactsModule {
    void addContact(AddContactInput input);

    void removeContact(RemoveContactInput input);

    ContactOutput getContact(GetContactInput input);

    PaginationOutput<ContactOutput> listContacts(SearchInput<ContactFilter> input);
}
