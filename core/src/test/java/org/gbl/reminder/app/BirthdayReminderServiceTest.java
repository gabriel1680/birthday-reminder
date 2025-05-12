package org.gbl.reminder.app;

import org.gbl.reminder.app.domain.NotificationRepository;
import org.gbl.reminder.fixture.FakeContactsModule;
import org.gbl.reminder.fixture.SpyEmailSender;
import org.gbl.reminder.out.notification.InMemoryNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.gbl.contacts.application.usecase.fixture.ContactFixture.toDate;

class BirthdayReminderServiceTest {

    private NotificationRepository notificationRepository;

    private SpyEmailSender sender;

    private BirthdayReminderService sut;

    @BeforeEach
    void setUp() {
        FakeContactsModule contactsModule = new FakeContactsModule();
        notificationRepository = new InMemoryNotificationRepository();
        sender = new SpyEmailSender();
        sut = new BirthdayReminderService(contactsModule, sender, notificationRepository);
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

    @Test
    void addNotificationMethod() {
        sut.addNotificationMethod("email", "jacob@gmail.com");
        assertThat(notificationRepository.all()).hasSize(1);
    }

    @Test
    void invalidNotificationMethod() {
        assertThatThrownBy(() -> sut.addNotificationMethod("a", "jacob@gmail.com"))
                .hasMessage("invalid notification type");
    }

    @Test
    void invalidNotificationValue() {
        assertThatThrownBy(() -> sut.addNotificationMethod("email", "b"))
                .hasMessage("invalid email");
    }
}