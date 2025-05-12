package org.gbl.in.di;

import com.google.inject.AbstractModule;
import org.gbl.common.service.json.GsonJsonParser;
import org.gbl.out.ContactsGateway;
import org.gbl.out.http.ContactsGatewayStub;
import org.gbl.out.http.HttpContactGateway;
import org.gbl.common.service.env.EnvManager;

import java.net.http.HttpClient;

public class ContactsModule extends AbstractModule {

    @Override
    protected void configure() {
        if (EnvManager.isTest()) {
            bindTest();
        } else {
            bindProduction();
        }
    }

    private void bindTest() {
        bind(ContactsGateway.class).to(ContactsGatewayStub.class);
    }

    private void bindProduction() {
        final var jsonParser = new GsonJsonParser();
        final var httpClient = HttpClient.newHttpClient();
        final var contactGateway =
                new HttpContactGateway(jsonParser, httpClient, "http://localhost:8080");
        bind(ContactsGateway.class).toInstance(contactGateway);
    }
}
