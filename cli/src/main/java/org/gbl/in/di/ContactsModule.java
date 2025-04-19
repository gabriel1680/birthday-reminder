package org.gbl.in.di;

import com.google.inject.AbstractModule;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.out.ContactsGateway;
import org.gbl.out.ContactResponse;
import org.gbl.out.http.HttpContactGateway;
import org.gbl.utils.Env;

import java.net.http.HttpClient;

public class ContactsModule extends AbstractModule {

    private static class ContactsGatewayMock implements ContactsGateway {
        @Override
        public ContactResponse create(CreateContactRequest request) {
            return new ContactResponse("1", request.name, request.birthdate);
        }

        @Override
        public ContactResponse get(String contactId) {
            return new ContactResponse("1", "Bella", "13/09/1987");
        }
    }

    @Override
    protected void configure() {
        if (Env.isTest()) {
            bind(ContactsGateway.class).to(ContactsGatewayMock.class);
        } else {
            final var httpClient = HttpClient.newHttpClient();
            final var contactGateway = new HttpContactGateway(httpClient, "https://localhost:8080");
            bind(ContactsGateway.class).toInstance(contactGateway);
        }
    }
}
