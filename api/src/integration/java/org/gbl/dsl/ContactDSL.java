package org.gbl.dsl;

import java.util.UUID;

import static org.gbl.dsl.BirthdayReminderDSL.BASE_URL;

public class ContactDSL {

    public static final String RESOURCE = "/contacts";

    public static final String RESOURCE_URL = BASE_URL + RESOURCE;

    public static class ITContactBuilder {
        private final String id = UUID.randomUUID().toString();
        private String name = "John Doe";
        private String birthdate = "1959-14-08T00:00:00Z";

        private ITContactBuilder() {
        }

        public static ITContactBuilder aContact() {
            return new ITContactBuilder();
        }

        public ITContactBuilder withName(String aName) {
            name = aName;
            return this;
        }

        public ITContactBuilder withBirthdate(String aBirthdate) {
            birthdate = aBirthdate;
            return this;
        }

        public ITContact build() {
            return new ITContact(id, name, birthdate);
        }
    }

    public record ITContact(String id, String name, String birthdate) {}
}
