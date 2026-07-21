package org.gbl;

import org.gbl.seeder.SeederFactory;

public final class Main {

    private static final int DEFAULT_COUNT = 10;
    private static final int MAX_COUNT = 1_000;
    private static final String DEFAULT_API_BASE_URL = "http://localhost:8080";

    private Main() {
    }

    public static void main(String[] args) throws Exception {
        final var count = args.length > 0 ? parseCount(args[0]) : DEFAULT_COUNT;
        final var apiBaseUrl = System.getenv().getOrDefault("API_BASE_URL", DEFAULT_API_BASE_URL);
        final var seeder = SeederFactory.create(apiBaseUrl);
        seeder.seed(count);
    }

    private static int parseCount(String value) {
        final int count;
        try {
            count = Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Count must be a positive integer.", exception);
        }
        if (count < 1 || count > MAX_COUNT) {
            throw new IllegalArgumentException("Count must be between 1 and " + MAX_COUNT + ".");
        }
        return count;
    }
}
