package org.gbl.seeder;

import com.google.gson.Gson;
import net.datafaker.Faker;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

public final class RootSeeder extends Seeder {

    private final List<Seeder> seeders;

    public RootSeeder(String apiBaseUrl, Gson gson, Faker faker, HttpClient httpClient,
                      List<Seeder> seeders) {
        super(apiBaseUrl, gson, faker, httpClient);
        this.seeders = List.copyOf(seeders);
    }

    @Override
    public void seed(int count) throws IOException, InterruptedException {
        for (var seeder : seeders) seeder.seed(count);
    }
}
