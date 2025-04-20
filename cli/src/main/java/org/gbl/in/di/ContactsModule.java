package org.gbl.in.di;

import com.google.inject.AbstractModule;
import org.gbl.out.ContactsGateway;
import org.gbl.out.http.ContactsGatewayStub;
import org.gbl.out.http.HttpContactGateway;
import org.gbl.utils.Env;

import java.net.http.HttpClient;

public class ContactsModule extends AbstractModule {

    @Override
    protected void configure() {
        if (Env.isTest()) {
            bindTest();
        } else {
            bindProduction();
        }
    }

    private void bindTest() {
        bind(ContactsGateway.class).to(ContactsGatewayStub.class);
    }

    private void bindProduction() {
        final var httpClient = HttpClient.newHttpClient();
        final var contactGateway = new HttpContactGateway(httpClient, "http://localhost:8080");
        bind(ContactsGateway.class).toInstance(contactGateway);
    }
}
