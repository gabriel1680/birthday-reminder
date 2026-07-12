package org.gbl;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.gbl.common.gateway.ResourceNotFoundException;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.notification.NotificationResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.gbl.DI.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebTest {

    private final ContactResponse AYRTON_SENNA = new ContactResponse("1", "Ayrton Senna", date("21/03/1960"));
    private final ContactResponse OZZY_OSBURN = new ContactResponse("2", "Ozzy Osburn", date("03/12/1948"));

    @Mock
    private ContactsGateway contactsGateway;

    @Mock
    private NotificationGateway notificationGateway;

    private Javalin server;

    @BeforeEach
    void setUp() {
        final var app = createWebApp(contactsGateway, notificationGateway);
        server = app.getServer();
    }

    private LocalDate date(String dateString) {
        final var pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, pattern);
    }

    @Nested
    class SearchContactsPage {

        @Test
        void should_send_headers() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.search(any())).thenReturn(success(Pagination.empty()));
                when(contactsGateway.getUpcomingBirthdays(any())).thenReturn(success(emptyList()));
                final var response = httpClient.get("/");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.code()).isEqualTo(200);
            });
        }

        @Test
        void should_render_with_contacts() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var contactResponses = List.of(AYRTON_SENNA, OZZY_OSBURN);
                final var pagination = new Pagination<>(1, 5, 2, 1, contactResponses);
                when(contactsGateway.search(any())).thenReturn(success(pagination));
                when(contactsGateway.getUpcomingBirthdays(any())).thenReturn(success(contactResponses));
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
        void should_handle_contacts_pagination() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var pagination = new Pagination<>(2, 1, 2, 2, List.of(AYRTON_SENNA));
                when(contactsGateway.search(any())).thenReturn(success(pagination));
                httpClient.get("/?page=2&size=1&order=asc");
                verify(contactsGateway, times(1)).search(requestCaptor.capture());
                final var value = requestCaptor.getValue();
                assertThat(value.page()).isEqualTo(2);
                assertThat(value.size()).isEqualTo(1);
                assertThat(value.order()).isEqualTo(SortingOrder.ASC);
            });
        }

        @Test
        void should_handle_filtering() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var pagination = new Pagination<>(2, 1, 2, 2, List.of(AYRTON_SENNA));
                when(contactsGateway.search(any())).thenReturn(success(pagination));
                httpClient.get("/?name=xyz&birthdateFrom=12/12/1900&birthdateTo=12/12/1999");
                verify(contactsGateway, times(1)).search(requestCaptor.capture());
                final var value = requestCaptor.getValue();
                assertThat(value.filter())
                        .isNotNull()
                        .extracting(ContactFilter::name, ContactFilter::birthdateFrom, ContactFilter::birthdateTo)
                        .containsExactly("xyz", "12/12/1900", "12/12/1999");
            });
        }

        @Test
        void should_handle_internal_server_error() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var randomError = new RuntimeException("Random error");
                when(contactsGateway.search(any())).thenReturn(failure(randomError));
                final var response = httpClient.get("/");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains("Internal Server Error");
                assertThat(response.code()).isEqualTo(500);
            });
        }
    }

    @Nested
    class ContactDetailsPage {

        @Test
        void should_handle_internal_server_error_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var randomError = new RuntimeException("Random error");
                when(contactsGateway.get(any())).thenReturn(failure(randomError));
                final var response = httpClient.get("/details/1");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains("Internal Server Error");
                assertThat(response.code()).isEqualTo(500);
            });
        }

        @Test
        void should_render_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.get(any())).thenReturn(success(AYRTON_SENNA));
                final var response = httpClient.get("/details/1");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains(AYRTON_SENNA.id());
                assertThat(response.code()).isEqualTo(200);
            });
        }

        @Test
        void should_render_not_found_for_invalid_contact_id() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.get(any())).thenReturn(failure(new ResourceNotFoundException("not found")));
                final var response = httpClient.get("/details/72");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains("Page not found");
                assertThat(response.code()).isEqualTo(404);
            });
        }
    }

    @Nested
    class NotificationsPage {

        @Test
        void should_handle_internal_server_error_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var randomError = new RuntimeException("Random error");
                when(notificationGateway.getAll()).thenReturn(failure(randomError));
                final var response = httpClient.get("/notifications");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains("Internal Server Error");
                assertThat(response.code()).isEqualTo(500);
            });
        }

        @Test
        void should_render_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var pushNotification = new NotificationResponse("1", "push", "push notification");
                when(notificationGateway.getAll()).thenReturn(success(List.of(pushNotification)));
                final var response = httpClient.get("/notifications");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains(pushNotification.id());
                assertThat(response.code()).isEqualTo(200);
            });
        }
    }
}
