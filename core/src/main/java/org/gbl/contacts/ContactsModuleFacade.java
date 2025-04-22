package org.gbl.contacts;

import org.gbl.contacts.application.usecase.add.AddContact;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.add.AddContactOutput;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.get.GetContact;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.contacts.application.usecase.list.ListContacts;
import org.gbl.contacts.application.usecase.remove.RemoveContact;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.contacts.application.usecase.update.UpdateContact;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;

public class ContactsModuleFacade implements ContactsModule {

    private final AddContact addContact;
    private final UpdateContact updateContact;
    private final RemoveContact removeContact;
    private final GetContact getContact;
    private final ListContacts listContacts;

    public ContactsModuleFacade(AddContact addContact, UpdateContact updateContact,
                                RemoveContact removeContact,
                                GetContact getContact, ListContacts listContacts) {
        this.addContact = addContact;
        this.updateContact = updateContact;
        this.removeContact = removeContact;
        this.getContact = getContact;
        this.listContacts = listContacts;
    }

    @Override
    public AddContactOutput addContact(AddContactInput input) {
        return addContact.execute(input);
    }

    @Override
    public void updateContact(UpdateContactInput input) {
        updateContact.execute(input);
    }

    @Override
    public void removeContact(RemoveContactInput input) {
        removeContact.execute(input);
    }

    @Override
    public ContactOutput getContact(GetContactInput input) {
        return getContact.execute(input);
    }

    @Override
    public PaginationOutput<ContactOutput> listContacts(SearchInput<ContactFilter> input) {
        return listContacts.execute(input);
    }
}
