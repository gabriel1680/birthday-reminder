package org.gbl.reminder.app;

import org.gbl.contacts.ContactsModule;
import org.gbl.contacts.application.service.query.PaginationOutput;
import org.gbl.contacts.application.usecase.get.ContactOutput;
import org.gbl.notification.NotificationModule;
import org.gbl.notification.application.NotificationResponse;
import org.gbl.reminder.out.email.EmailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.gbl.contacts.application.usecase.fixture.ContactFixture.toDate;
import static org.gbl.reminder.Fixture.DONALD;
import static org.gbl.reminder.Fixture.IAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BirthdayReminderServiceTest {

    private final static NotificationResponse NOTIFICATION_RESPONSE =
            new NotificationResponse(UUID.randomUUID().toString(), "email", "j@gmail.com");

    @Mock
    private ContactsModule contactsModule;

    @Mock
    private NotificationModule notificationModule;

    @Mock
    private EmailSender sender;

    @InjectMocks
    private BirthdayReminderService sut;

    private void givenContacts(List<ContactOutput> contacts) {
        final var output = new PaginationOutput<>(1, 1, contacts.size(), contacts);
        when(contactsModule.listContacts(any())).thenReturn(output);
        when(notificationModule.all()).thenReturn(List.of(NOTIFICATION_RESPONSE));
    }

    @Test
    void noUsersBirthday() {
        when(contactsModule.listContacts(any())).thenReturn(PaginationOutput.emptyOf(1, 1));
        sut.remindOf(toDate("19/09/1999"));
        verify(sender, never()).send(any());
    }

    @Test
    void oneUsersBirthday() {
        final var contacts = List.of(IAN);
        givenContacts(contacts);
        sut.remindOf(toDate("22/12/2000"));
        verify(sender, times(1)).send(any());
    }

    @Test
    void multipleUsersBirthday() {
        givenContacts(List.of(IAN, DONALD));
        sut.remindOf(toDate("24/12/2000"));
        verify(sender, times(2)).send(any());
    }
}