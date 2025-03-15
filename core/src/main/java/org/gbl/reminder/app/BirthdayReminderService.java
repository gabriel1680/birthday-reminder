package org.gbl.reminder.app;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.usecase.get.ContactOutput;
import org.gbl.contacts.usecase.list.ContactFilter;
import org.gbl.reminder.out.email.EmailSender;
import org.gbl.reminder.out.email.SendEmailRequest;
import org.gbl.shared.PaginationOutput;
import org.gbl.shared.SearchInput;
import org.gbl.shared.SortingOrder;

import java.time.LocalDate;

public class BirthdayReminderService {

    private final ContactsModule contactsModule;
    private final EmailSender emailSender;

    public BirthdayReminderService(ContactsModule contactsModule, EmailSender emailSender) {
        this.contactsModule = contactsModule;
        this.emailSender = emailSender;
    }

    public void remindOf(LocalDate today) {
        final var output = contactsModule.listContacts(createSearchInput(today));
        if (output.total() == 0) return;
        sendEmails(today, output);
    }

    private static SearchInput<ContactFilter> createSearchInput(LocalDate today) {
        final var filter = new ContactFilter(null, today);
        return new SearchInput<>(1, 1, 0, SortingOrder.ASC, filter);
    }

    private void sendEmails(LocalDate today, PaginationOutput<ContactOutput> output) {
        for (final var contact : output.values())
            emailSender.send(createSendMailRequest(today, contact));
    }

    private static SendEmailRequest createSendMailRequest(LocalDate today, ContactOutput contact) {
        return new SendEmailRequest(
                "Send Happy Birthday To A Friend!",
                ("Today %s is birthday of %s").formatted(today, contact.name()));
    }
}
