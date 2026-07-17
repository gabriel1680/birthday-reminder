package org.gbl.reminder.app;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.gbl.contacts.application.service.query.ContactFilter;
import org.gbl.notification.NotificationsModule;
import org.gbl.reminder.out.email.EmailSender;
import org.gbl.reminder.out.email.SendEmailRequest;

import java.time.LocalDate;

public class BirthdayReminderService {

    private final ContactsModule contactsModule;
    private final EmailSender emailSender;
    private final NotificationsModule notificationsModule;

    public BirthdayReminderService(ContactsModule contactsModule, EmailSender emailSender,
                                   NotificationsModule notificationsModule) {
        this.contactsModule = contactsModule;
        this.emailSender = emailSender;
        this.notificationsModule = notificationsModule;
    }

    public void remindOf(final LocalDate today) {
        final var output = contactsModule.search(createSearchInput(today));
        if (output.total() == 0) {
            return;
        }
        final var notificationMethods = notificationsModule.all();
        if (notificationMethods.stream().anyMatch(n -> n.type().equals("email"))) {
            sendEmails(today, output);
        }
    }

    private static SearchInput<ContactFilter> createSearchInput(LocalDate today) {
        final var filter = ContactFilter.of(today, today);
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
