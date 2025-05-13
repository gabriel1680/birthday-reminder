package org.gbl.notification.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationTest {

    @Test
    void update() {
        var notification = Notification.create("email", "jacob@gmail.com");
        notification.update("l@gmail.com");
        assertThat(notification.value()).isEqualTo("l@gmail.com");
    }

    @Test
    void updateWithInvalidEmail() {
        var notification = Notification.create("email", "jacob@gmail.com");
        assertThatThrownBy(() -> notification.update("lgmail.com"))
                .hasMessage("invalid email");
    }

    @Test
    void invalidNotificationMethod() {
        assertThatThrownBy(() -> Notification.create("a", "jacob@gmail.com"))
                .hasMessage("invalid notification type");
    }

    @Test
    void invalidNotificationValue() {
        assertThatThrownBy(() -> Notification.create("email", "b"))
                .hasMessage("invalid email");
    }
}