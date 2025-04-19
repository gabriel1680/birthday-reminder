package org.gbl.in.di;

import com.google.inject.AbstractModule;
import org.gbl.in.CreateContact.CreateContactRequest;
import org.gbl.out.ContactsGateway;
import org.gbl.out.CreateContactResponse;
import org.gbl.out.http.HttpContactGateway;
import org.gbl.utils.Env;

import java.net.http.HttpClient;

public class ContactsModule extends AbstractModule {

    @Override
    protected void configure() {
        if (Env.isTest()) {
            bind(ContactsGateway.class).toInstance(new ContactsGateway() {

                @Override
                public CreateContactResponse create(CreateContactRequest request) {
                    return new CreateContactResponse(request.name, request.birthdate);
                }
            });
        } else {
            final var httpClient = HttpClient.newHttpClient();
            final var contactGateway = new HttpContactGateway(httpClient, "https://localhost:8080");
            bind(ContactsGateway.class).toInstance(contactGateway);
        }
    }
}
