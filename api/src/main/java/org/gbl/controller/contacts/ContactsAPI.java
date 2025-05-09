package org.gbl.controller.contacts;

import org.gbl.controller.HttpAPIResponse;
import spark.Request;
import spark.Response;

public interface ContactsAPI {
    HttpAPIResponse createContact(Request request, Response response);

    HttpAPIResponse getContract(Request request, Response response);

    HttpAPIResponse deleteContact(Request request, Response response);

    HttpAPIResponse updateContact(Request request, Response response);

    HttpAPIResponse searchContacts(Request request, Response response);
}
