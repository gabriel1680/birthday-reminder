package org.gbl.reminder.app;

import org.gbl.reminder.fixture.FakeContactsModule;
import org.gbl.reminder.fixture.FakeNotificationModule;
import org.gbl.reminder.fixture.SpyEmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.toDate;

class BirthdayReminderServiceTest {

    private SpyEmailSender sender;

    private BirthdayReminderService sut;

    @BeforeEach
    void setUp() {
        FakeContactsModule contactsModule = new FakeContactsModule();
        FakeNotificationModule notificationModule = new FakeNotificationModule();
        sender = new SpyEmailSender();
        sut = new BirthdayReminderService(contactsModule, sender, notificationModule);
    }

    @Test
    void noUsersBirthday() {
        sut.remindOf(toDate("19/09/1999"));
        assertThat(sender.mailsSent().size()).isEqualTo(0);
    }

    @Test
    void oneUsersBirthday() {
        sut.remindOf(toDate("22/12/2000"));
        assertThat(sender.mailsSent().size()).isEqualTo(1);
    }

    @Test
    void multipleUsersBirthday() {
        sut.remindOf(toDate("24/12/2000"));
        assertThat(sender.mailsSent().size()).isEqualTo(2);
    }
}