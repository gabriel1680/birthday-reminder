package org.gbl;

import static org.gbl.DI.createWebApp;
import static org.gbl.DI.httpContactGateway;

public class Main {

    public static void main(String[] args) {
        final var web = createWebApp(httpContactGateway());
        web.start(9090);
    }
}