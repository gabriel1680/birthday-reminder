package org.gbl.contacts;

import org.gbl.contacts.application.service.query.ContactFilter;
import org.gbl.contacts.application.service.query.ContactQueryRepository;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.usecase.add.AddContact;
import org.gbl.contacts.application.usecase.add.AddContactInput;
import org.gbl.contacts.application.usecase.get.GetContact;
import org.gbl.contacts.application.usecase.get.GetContactInput;
import org.gbl.contacts.application.usecase.remove.RemoveContact;
import org.gbl.contacts.application.usecase.remove.RemoveContactInput;
import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.gbl.contacts.application.usecase.upcoming_birthdays.GetUpcomingBirthdays;
import org.gbl.contacts.application.usecase.upcoming_birthdays.GetUpcomingBirthdaysInput;
import org.gbl.contacts.application.usecase.update.UpdateContact;
import org.gbl.contacts.application.usecase.update.UpdateContactInput;

import java.util.Collection;

public class ContactsModuleFacade implements ContactsModule {

    private final AddContact addContact;
    private final UpdateContact updateContact;
    private final RemoveContact removeContact;
    private final GetContact getContact;
    private final GetUpcomingBirthdays getUpcomingBirthdays;
    private final ContactQueryRepository queryRepository;

    public ContactsModuleFacade(AddContact addContact,
                                UpdateContact updateContact,
                                RemoveContact removeContact,
                                GetContact getContact,
                                GetUpcomingBirthdays getUpcomingBirthdays,
                                ContactQueryRepository queryRepository) {
        this.addContact = addContact;
        this.updateContact = updateContact;
        this.removeContact = removeContact;
        this.getContact = getContact;
        this.getUpcomingBirthdays = getUpcomingBirthdays;
        this.queryRepository = queryRepository;
    }

    @Override
    public ContactOutput addContact(AddContactInput input) {
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
    public PaginationOutput<ContactOutput> search(SearchInput<ContactFilter> input) {
        return queryRepository.search(input);
    }

    @Override
    public Collection<ContactOutput> upcomingBirthdays(GetUpcomingBirthdaysInput input) {
        return getUpcomingBirthdays.execute(input);
    }
}
