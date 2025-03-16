package org.gbl.contacts;

import org.gbl.contacts.usecase.add.AddContact;
import org.gbl.contacts.usecase.add.AddContactInput;
import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.contacts.usecase.get.GetContact;
import org.gbl.contacts.usecase.get.GetContactInput;
import org.gbl.contacts.usecase.list.ContactFilter;
import org.gbl.contacts.usecase.list.ListContacts;
import org.gbl.contacts.usecase.remove.RemoveContact;
import org.gbl.contacts.usecase.remove.RemoveContactInput;
import org.gbl.contacts.usecase.update.UpdateContact;
import org.gbl.contacts.usecase.update.UpdateContactInput;
import org.gbl.shared.PaginationOutput;
import org.gbl.shared.SearchInput;

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
    public void addContact(AddContactInput input) {
        addContact.execute(input);
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
