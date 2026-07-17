package org.gbl;

import org.gbl.common.service.json.GsonJsonAdapter;

public class Main {

    public static void main(String[] args) {
        final var jsonAdapter = new GsonJsonAdapter();
        final var api = new BReminderAPI(jsonAdapter);
        api.start();
    }
}