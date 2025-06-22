package org.gbl.contacts;

import org.gbl.contacts.application.service.query.ContactFilter;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;

public interface ContactsModule {
    ContactOutput addContact(AddContactInput input);

    void updateContact(UpdateContactInput input);

    void removeContact(RemoveContactInput input);

    ContactOutput getContact(GetContactInput input);

    PaginationOutput<ContactOutput> search(SearchInput<ContactFilter> input);
}
