package org.gbl;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import io.vavr.control.Try;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebTest {

    private final ContactResponse AYRTON_SENNA = new ContactResponse("1", "Ayrton Senna", "21/03" +
            "/1960");
    private final ContactResponse OZZY_OSBURN = new ContactResponse("2", "Ozzy Osburn", "03/12" +
            "/1948");

    @Mock
    private ContactsGateway contactsGateway;

    private Javalin server;

    @BeforeEach
    void setUp() {
        final var web = new Web(contactsGateway);
        server = web.getServer();
    }

    @Test
    void contacts_page() {
        JavalinTest.test(server, (server, httpClient) -> {
            when(contactsGateway.search(any())).thenReturn(Try.success(Pagination.empty()));
            final var response = httpClient.get("/");
            assertThat(response.header("Content-Type")).isEqualTo("text/html");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void contacts_page_content() {
        JavalinTest.test(server, (server, httpClient) -> {
            final var pagination = new Pagination<>(1, 5, 2, 1, List.of(AYRTON_SENNA, OZZY_OSBURN));
            when(contactsGateway.search(any())).thenReturn(Try.success(pagination));
            final var response = httpClient.get("/");
            assert response.body() != null;
            final var htmlContent = response.body().string();
            assertThat(htmlContent).contains("Ozzy");
            assertThat(htmlContent).contains("03/12/1948");
        });
    }

    @Captor
    private ArgumentCaptor<SearchRequest<ContactFilter>> requestCaptor;

    @Test
    void contacts_page_pagination() {
        JavalinTest.test(server, (server, httpClient) -> {
            final var pagination = new Pagination<>(2, 1, 2, 2, List.of(AYRTON_SENNA));
            when(contactsGateway.search(any())).thenReturn(Try.success(pagination));
            httpClient.get("/?page=2&size=1&order=asc");
            verify(contactsGateway, times(1)).search(requestCaptor.capture());
            final var value = requestCaptor.getValue();
            assertThat(value.page()).isEqualTo(2);
            assertThat(value.size()).isEqualTo(1);
            assertThat(value.order()).isEqualTo(SortingOrder.ASC);
            assertThat(value.filter()).isNull();
        });
    }

    @Test
    void contacts_page_error() {
        JavalinTest.test(server, (server, httpClient) -> {
            final var randomError = new RuntimeException("Random error");
            when(contactsGateway.search(any())).thenReturn(Try.failure(randomError));
            final var response = httpClient.get("/");
            assertThat(response.header("Content-Type")).isEqualTo("text/html");
            assertThat(response.body().string()).contains("Internal Server Error");
            assertThat(response.code()).isEqualTo(500);
        });
    }
}
