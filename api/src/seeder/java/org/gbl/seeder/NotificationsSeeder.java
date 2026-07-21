package org.gbl.seeder;

import com.google.gson.Gson;
import net.datafaker.Faker;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.HashSet;

public final class NotificationsSeeder extends Seeder {

    private static final String ENDPOINT = "/notifications";

    public NotificationsSeeder(String apiBaseUrl, Gson gson, Faker faker, HttpClient httpClient) {
        super(apiBaseUrl, gson, faker, httpClient);
    }

    @Override
    public void seed(int count) throws IOException, InterruptedException {
        ensureApiIsAvailable(ENDPOINT);
        seedContacts(count);
        System.out.printf("Successfully created %d email notifications through the API.%n", count);
    }

    private void seedContacts(int count) throws IOException, InterruptedException {
        final var emails = new HashSet<String>();
        while (emails.size() < count) {
            final var email = faker.internet().emailAddress();
            if (!emails.add(email)) {
                continue;
            }
            post(ENDPOINT, new NotificationPayload("email", email));
            System.out.printf("Creating email notification %d/%d...%n", emails.size(), count);
        }
    }

    private record NotificationPayload(String type, String value) {
    }
}
