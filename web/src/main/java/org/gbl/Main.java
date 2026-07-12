package org.gbl;

import static org.gbl.DI.*;
import static org.gbl.DI.createWebApp;
import static org.gbl.DI.httpContactGateway;

public class Main {

    public static void main(String[] args) {
        final var web = createWebApp(httpContactGateway(), notificationGateway());
        web.start(9090);
    }
}