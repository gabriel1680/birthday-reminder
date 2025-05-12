package org.gbl.reminder.app;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.service.query.SearchInput;
import org.gbl.contacts.application.service.query.SortingOrder;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.contacts.application.usecase.list.ContactFilter;
import org.gbl.reminder.out.email.EmailSender;
import org.gbl.reminder.out.email.SendEmailRequest;

import java.time.LocalDate;

public class BirthdayReminderService {

    private final ContactsModule contactsModule;
    private final EmailSender emailSender;
    private final NotificationRepository notificationRepository;
    private final NotificationMethodFactory notificationMethodFactory;

    public BirthdayReminderService(ContactsModule contactsModule, EmailSender emailSender,
                                   NotificationRepository notificationRepository) {
        this.contactsModule = contactsModule;
        this.emailSender = emailSender;
        this.notificationRepository = notificationRepository;
        notificationMethodFactory = new NotificationMethodFactory();
    }

    public void remindOf(LocalDate today) {
        final var output = contactsModule.listContacts(createSearchInput(today));
        if (output.total() == 0) return;
        sendEmails(today, output);
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

    public void addNotificationMethod(String notificationType, String notificationValue) {
        final var notificationMethod = notificationMethodFactory.create(notificationType,
                                                                        notificationValue);
        notificationRepository.add(notificationMethod);
    }
}
