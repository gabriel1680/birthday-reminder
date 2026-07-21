package org.gbl.seeder;

import com.google.gson.Gson;
import net.datafaker.Faker;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Locale;

public final class SeederFactory {

    private SeederFactory() {
    }

    public static Seeder create(String apiBaseUrl) {
        final var gson = new Gson();
        final var faker = new Faker(Locale.forLanguageTag("en"));
        final var httpClient = HttpClient.newHttpClient();
        final var seeders = List.<Seeder>of(
                new ContactsSeeder(apiBaseUrl, gson, faker, httpClient),
                new NotificationsSeeder(apiBaseUrl, gson, faker, httpClient)
        );
        return new RootSeeder(apiBaseUrl, gson, faker, httpClient, seeders);
    }
}
