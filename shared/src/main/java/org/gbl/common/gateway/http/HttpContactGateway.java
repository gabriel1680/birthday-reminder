package org.gbl.common.gateway.http;

import com.google.gson.reflect.TypeToken;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.CreateContactRequest;
import org.gbl.common.gateway.GetUpcomingBirthdaysRequest;
import org.gbl.common.gateway.UpdateContactRequest;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpContactGateway implements ContactsGateway {

    private static final String RESOURCE = "/contacts";

    private static final Type CONTACT_RESPONSE_TYPE = ContactResponse.class;
    private static final Type PAGINATION_RESPONSE_TYPE =
            TypeToken.getParameterized(Pagination.class, ContactResponse.class).getType();
    private static final Type UPCOMING_RESPONSE_TYPE =
            TypeToken.getParameterized(List.class, ContactResponse.class).getType();

    private final HttpApiClient httpApiClient;

    public HttpContactGateway(HttpApiClient gateway) {
        this.httpApiClient = gateway;
    }

    @Override
    public ContactResponse get(String contactId) {
        final var path = RESOURCE + "/" + contactId;
        return httpApiClient.get(path, CONTACT_RESPONSE_TYPE);
    }

    @Override
    public ContactResponse create(CreateContactRequest request) {
        return httpApiClient.post(RESOURCE, request, CONTACT_RESPONSE_TYPE);
    }

    @Override
    public void update(UpdateContactRequest request) {
        final var path = RESOURCE + "/" + request.id();
        httpApiClient.put(path, request, CONTACT_RESPONSE_TYPE);
    }

    @Override
    public void delete(String contactId) {
        final var path = RESOURCE + "/" + contactId;
        httpApiClient.delete(path, CONTACT_RESPONSE_TYPE);
    }

    @Override
    public Pagination<ContactResponse> search(SearchRequest<ContactFilter> searchRequest) {
        final var path = RESOURCE + "?" + toString(searchRequest);
        return httpApiClient.get(path, PAGINATION_RESPONSE_TYPE);
    }

    @Override
    public List<ContactResponse> getUpcomingBirthdays(GetUpcomingBirthdaysRequest request) {
        final var path = RESOURCE + "/upcoming-birthdays?size=" + request.size();
        final var headers = Map.of("X-Time-Zone", request.clientZoneId().getId());
        return httpApiClient.get(path, UPCOMING_RESPONSE_TYPE, headers);
    }

    private String toString(SearchRequest<ContactFilter> searchRequest) {
        final var paginationParams = List.of("page=" + searchRequest.page(),
                                             "size=" + searchRequest.size(),
                                             "order=" + searchRequest.order());
        var params = new ArrayList<>(paginationParams);
        return String.join("&", params);
    }
}
