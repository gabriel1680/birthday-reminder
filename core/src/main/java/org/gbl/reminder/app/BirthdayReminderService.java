package org.gbl.reminder.app;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.notification.NotificationModule;
import org.gbl.reminder.out.email.EmailSender;
import org.gbl.reminder.out.email.SendEmailRequest;

import java.time.LocalDate;

public class BirthdayReminderService {

    private final ContactsModule contactsModule;
    private final EmailSender emailSender;
    private final NotificationModule notificationModule;

    public BirthdayReminderService(ContactsModule contactsModule, EmailSender emailSender,
                                   NotificationModule notificationModule) {
        this.contactsModule = contactsModule;
        this.emailSender = emailSender;
        this.notificationModule = notificationModule;
    }

    public void remindOf(final LocalDate today) {
        final var output = contactsModule.listContacts(createSearchInput(today));
        if (output.total() == 0) {
            return;
        }
        final var notificationMethods = notificationModule.all();
        if (notificationMethods.stream().anyMatch(n -> n.type().equals("email"))) {
            sendEmails(today, output);
        }
    }

    private static SearchInput<ContactFilter> createSearchInput(LocalDate today) {
        final var filter = new ContactFilter(null, today);
        return new SearchInput<>(1, 1, SortingOrder.ASC, filter);
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
