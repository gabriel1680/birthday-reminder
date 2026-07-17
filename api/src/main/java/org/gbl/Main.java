package org.gbl;

import org.gbl.common.service.json.GsonJsonServiceAdapter;

public class Main {

    public static void main(String[] args) {
        final var jsonAdapter = new GsonJsonServiceAdapter();
        final var api = new BReminderAPI(jsonAdapter);
        api.start();
    }
}