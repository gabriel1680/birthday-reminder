package org.gbl.seeder;

import com.google.gson.Gson;
import net.datafaker.Faker;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public final class ContactsSeeder extends Seeder {

    private static final String ENDPOINT = "/contacts";

    public ContactsSeeder(String apiBaseUrl, Gson gson, Faker faker, HttpClient httpClient) {
        super(apiBaseUrl, gson, faker, httpClient);
    }

    @Override
    public void seed(int count) throws IOException, InterruptedException {
        ensureApiIsAvailable(ENDPOINT);
        seedContacts(count);
        System.out.printf("Successfully created %d contacts through the API.%n", count);
    }

    private void seedContacts(int count) throws IOException, InterruptedException {
        final var names = new HashSet<String>();
        final var birthdays = new HashSet<LocalDate>();
        while (names.size() < count) {
            final var name = faker.name().fullName();
            final var birthdate = randomBirthdate();
            if (names.contains(name) || birthdays.contains(birthdate)) {
                continue;
            }
            names.add(name);
            birthdays.add(birthdate);
            post(ENDPOINT, new ContactPayload(name, birthdate.toString()));
            System.out.printf("Creating contact %d/%d...%n", names.size(), count);
        }
    }

    private static LocalDate randomBirthdate() {
        final var today = LocalDate.now();
        final var earliest = today.minusYears(100).toEpochDay();
        final var latest = today.minusYears(1).toEpochDay();
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(earliest, latest + 1));
    }

    private record ContactPayload(String name, String birthdate) {
    }
}
