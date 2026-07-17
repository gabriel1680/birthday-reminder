package org.gbl.notification.application;

import org.gbl.notification.domain.Notification;
import org.gbl.reminder.out.notification.InMemoryNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationServiceTest {

    private InMemoryNotificationRepository repository;

    private NotificationService sut;

    @BeforeEach
    void setUp() {
        repository = new InMemoryNotificationRepository();
        sut = new NotificationService(repository);
    }

    @Test
    void add() {
        var request = new AddNotificationInput("email", "jacob@gmail.com");
        sut.add(request);
        assertThat(repository.all()).hasSize(1);
    }

    @Test
    void all() {
        var notification = Notification.create("email", "jacob@gmail.com");
        repository.add(notification);
        assertThat(sut.all()).hasSize(1);
    }

    @Test
    void removeOf() {
        var notification = Notification.create("email", "jacob@gmail.com");
        repository.add(notification);
        sut.removeOf(notification.id());
        assertThat(repository.all()).hasSize(0);
    }

    @Test
    void notFound() {
        assertThatThrownBy(() -> sut.removeOf(""))
                .isInstanceOf(NotificationNotFoundException.class);
    }
}