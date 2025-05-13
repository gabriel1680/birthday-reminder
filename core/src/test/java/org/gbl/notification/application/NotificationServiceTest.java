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
        var request = new AddNotificationRequest("email", "jacob@gmail.com");
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
    void remove() {
        var notification = Notification.create("email", "jacob@gmail.com");
        repository.add(notification);
        sut.remove(notification.id());
        assertThat(repository.all()).hasSize(0);
    }

    @Test
    void update() {
        var notification = Notification.create("email", "jacob@gmail.com");
        repository.add(notification);
        var request = new UpdateNotificationRequest(notification.id(), "j@gmail.com");
        sut.update(request);
        assertThat(repository.getById(notification.id()))
                .get()
                .extracting(Notification::value)
                .isEqualTo("j@gmail.com");
    }

    @Test
    void notFound() {
        var request = new UpdateNotificationRequest("invalid", "j@gmail.com");
        assertThatThrownBy(() -> sut.update(request))
                .isInstanceOf(NotificationNotFoundException.class);
        assertThatThrownBy(() -> sut.remove(request.id()))
                .isInstanceOf(NotificationNotFoundException.class);
    }
}