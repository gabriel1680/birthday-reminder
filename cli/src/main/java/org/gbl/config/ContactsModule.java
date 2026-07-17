package org.gbl.config;

import com.google.inject.AbstractModule;
import org.gbl.common.gateway.http.HttpApiClient;
import org.gbl.common.service.json.GsonJsonAdapter;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.memory.ContactsGatewayStub;
import org.gbl.common.gateway.http.HttpContactGateway;
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
        final var jsonParser = new GsonJsonAdapter();
        final var httpClient = HttpClient.newHttpClient();
        final var restClient = new HttpApiClient(jsonParser, httpClient, "http://localhost:8080");
        final var contactGateway = new HttpContactGateway(restClient);
        bind(ContactsGateway.class).toInstance(contactGateway);
    }
}
