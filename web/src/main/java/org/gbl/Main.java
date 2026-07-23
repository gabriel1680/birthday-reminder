package org.gbl;

import static org.gbl.config.DI.createWebApp;
import static org.gbl.config.DI.httpContactGateway;
import static org.gbl.config.DI.notificationGateway;

public class Main {

    public static void main(String[] args) {
        final var web = createWebApp(httpContactGateway(), notificationGateway());
        web.start(9090);
    }
}