package org.gbl.dsl;

import java.util.UUID;

import static org.gbl.dsl.BirthdayReminderDSL.BASE_URL;

public class NotificationDSL {

    public static final String RESOURCE = "/notifications";

    public static final String RESOURCE_URL = BASE_URL + RESOURCE;

    public static class ITNotificationBuilder {
        private final String id = UUID.randomUUID().toString();
        private String type = "e-mail";
        private String value = "john.doe@gmail.com";

        private ITNotificationBuilder() {
        }

        public static ITNotificationBuilder aNotification() {
            return new ITNotificationBuilder();
        }

        public ITNotificationBuilder withType(String aType) {
            type = aType;
            return this;
        }

        public ITNotificationBuilder withValue(String aValue) {
            value = aValue;
            return this;
        }

        public ITNotification build() {
            return new ITNotification(id, type, value);
        }
    }

    public record ITNotification(String id, String type, String value) {}
}
