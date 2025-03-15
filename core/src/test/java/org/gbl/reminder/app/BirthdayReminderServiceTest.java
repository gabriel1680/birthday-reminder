package org.gbl.reminder.app;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gbl.contacts.usecase.fixture.ContactFixture.toDate;

class BirthdayReminderServiceTest {

    private final FakeContactsModule contactsModule = new FakeContactsModule();

    private final SpyEmailSender sender = new SpyEmailSender();

    @Test
    void noUsersBirthday() {
        final var sut = new BirthdayReminderService(contactsModule, sender);
        sut.remindOf(toDate("19/09/1999"));
        assertThat(sender.mailsSent().size()).isEqualTo(0);
    }

    @Test
    void oneUsersBirthday() {
        final var sut = new BirthdayReminderService(contactsModule, sender);
        sut.remindOf(toDate("22/12/2000"));
        assertThat(sender.mailsSent().size()).isEqualTo(1);
    }

    @Test
    void multipleUsersBirthday() {
        final var sut = new BirthdayReminderService(contactsModule, sender);
        sut.remindOf(toDate("24/12/2000"));
        assertThat(sender.mailsSent().size()).isEqualTo(2);
    }
}