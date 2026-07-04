package org.gbl;

public class Main {

    public static void main(String[] args) {
        final var web = DI.createWebApp(DI.httpContactGateway());
        web.start(9090);
    }
}