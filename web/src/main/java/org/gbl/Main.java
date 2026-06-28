package org.gbl;

import org.gbl.common.service.json.GsonJsonParser;
import org.gbl.common.gateway.http.HttpContactGateway;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        final var httpClient = HttpClient.newHttpClient();
        final var jsonParser = new GsonJsonParser();
        final var gateway = new HttpContactGateway(jsonParser, httpClient, "");
        final var web = new Web(gateway);
        web.getServer().start(8080);
    }
}