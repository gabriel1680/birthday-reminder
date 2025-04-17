package org.gbl.in.di;

import com.google.inject.AbstractModule;
import org.gbl.out.ContactsGateway;
import org.gbl.out.http.HttpContactGateway;

import java.net.http.HttpClient;

public class ContactsModule extends AbstractModule {

    @Override
    protected void configure() {
        final var httpClient = HttpClient.newHttpClient();
        final var contactGateway = new HttpContactGateway(httpClient, "https://localhost:8080");
        bind(ContactsGateway.class).toInstance(contactGateway);
    }
}
