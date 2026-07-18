package org.gbl;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.CreateContactRequest;
import org.gbl.common.gateway.ResourceNotFoundException;
import org.gbl.common.notification.AddNotificationRequest;
import org.gbl.common.notification.NotificationGateway;
import org.gbl.common.notification.NotificationResponse;
import org.gbl.common.notification.RemoveNotificationRequest;
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

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.gbl.DI.createWebApp;
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
    class HomePage {

        @Test
        void should_render_upcoming_birthdays() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.getUpcomingBirthdays(any())).thenReturn(List.of(AYRTON_SENNA));

                final var response = httpClient.get("/");

                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string())
                        .contains("Birthday Reminder")
                        .contains("Upcoming Birthdays")
                        .contains(AYRTON_SENNA.name());
                assertThat(response.code()).isEqualTo(200);
            });
        }
    }

    @Nested
    class SearchContactsPage {

        @Test
        void should_send_headers() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.search(any())).thenReturn(Pagination.empty());
                final var response = httpClient.get("/contacts");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.code()).isEqualTo(200);
            });
        }

        @Test
        void should_show_add_contact_action_when_the_list_is_empty() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.search(any())).thenReturn(Pagination.empty());

                final var response = httpClient.get("/contacts");

                assertThat(response.body().string())
                        .contains("+ Add contact")
                        .contains("href=\"/contacts/new\"");
            });
        }

        @Test
        void should_render_with_contacts() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var contactResponses = List.of(AYRTON_SENNA, OZZY_OSBURN);
                final var pagination = new Pagination<>(1, 5, 2, 1, contactResponses);
                when(contactsGateway.search(any())).thenReturn(pagination);
                final var response = httpClient.get("/contacts");
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
                when(contactsGateway.search(any())).thenReturn(pagination);
                httpClient.get("/contacts?page=2&size=1&order=asc");
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
                when(contactsGateway.search(any())).thenReturn(pagination);
                httpClient.get("/contacts?name=xyz&birthdateFrom=12/12/1900&birthdateTo=12/12/1999");
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
                when(contactsGateway.search(any())).thenThrow(randomError);
                final var response = httpClient.get("/contacts");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains("Internal Server Error");
                assertThat(response.code()).isEqualTo(500);
            });
        }
    }

    @Nested
    class CreateContactPage {

        @Test
        void should_render_the_create_form() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var response = httpClient.get("/contacts/new");

                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains("New contact")
                        .contains("Create contact")
                        .contains("name=\"birthdate\"");
            });
        }

        @Test
        void should_create_a_contact_and_redirect_to_its_details() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.create(any())).thenReturn(AYRTON_SENNA);
                when(contactsGateway.get(AYRTON_SENNA.id())).thenReturn(AYRTON_SENNA);
                final var requestCaptor = ArgumentCaptor.forClass(CreateContactRequest.class);

                final var response = httpClient.post(
                        "/contacts",
                        "name=Ayrton+Senna&birthdate=1960-03-21");

                verify(contactsGateway).create(requestCaptor.capture());
                assertThat(requestCaptor.getValue())
                        .extracting(CreateContactRequest::name, CreateContactRequest::birthdate)
                        .containsExactly("Ayrton Senna", LocalDate.parse("1960-03-21"));
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.request().url().encodedPath()).isEqualTo("/contacts/1");
                assertThat(response.body().string()).contains(AYRTON_SENNA.name());
            });
        }

        @Test
        void should_show_validation_errors_and_preserve_values() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var response = httpClient.post(
                        "/contacts",
                        "name=++&birthdate=not-a-date");

                assertThat(response.code()).isEqualTo(400);
                assertThat(response.body().string())
                        .contains("Enter a contact name.")
                        .contains("Enter a valid birthday.")
                        .contains("value=\"not-a-date\"");
            });
        }
    }

    @Nested
    class ContactDetailsPage {

        @Test
        void should_handle_internal_server_error_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var randomError = new RuntimeException("Random error");
                when(contactsGateway.get(any())).thenThrow(randomError);
                final var response = httpClient.get("/contacts/1");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains("Internal Server Error");
                assertThat(response.code()).isEqualTo(500);
            });
        }

        @Test
        void should_render_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.get(any())).thenReturn(AYRTON_SENNA);
                final var response = httpClient.get("/contacts/1");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains(AYRTON_SENNA.id());
                assertThat(response.code()).isEqualTo(200);
            });
        }

        @Test
        void should_render_not_found_for_invalid_contact_id() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.get(any())).thenThrow(new ResourceNotFoundException("not found"));
                final var response = httpClient.get("/contacts/72");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string()).contains("Page not found");
                assertThat(response.code()).isEqualTo(404);
            });
        }

        @Test
        void should_delete_contact_and_return_to_contacts() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(contactsGateway.search(any())).thenReturn(Pagination.empty());

                final var response = httpClient.post("/contacts/1/delete");

                verify(contactsGateway).delete("1");
                assertThat(response.body().string()).contains("No contacts found");
                assertThat(response.code()).isEqualTo(200);
            });
        }
    }

    @Nested
    class NotificationsPage {

        @Test
        void should_handle_internal_server_error_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var randomError = new RuntimeException("Random error");
                when(notificationGateway.getAll()).thenThrow(randomError);
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
                when(notificationGateway.getAll()).thenReturn(List.of(pushNotification));
                final var response = httpClient.get("/notifications");
                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string())
                        .contains(pushNotification.type())
                        .contains(pushNotification.value());
                assertThat(response.code()).isEqualTo(200);
            });
        }

        @Test
        void should_render_empty_state_when_no_notifications_are_configured() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(notificationGateway.getAll()).thenReturn(emptyList());

                final var response = httpClient.get("/notifications");

                assertThat(response.body().string())
                        .contains("No notifications configured")
                        .contains("+ Add notification")
                        .contains("href=\"/notifications/new\"");
                assertThat(response.code()).isEqualTo(200);
            });
        }
    }

    @Nested
    class CreateNotificationPage {

        @Test
        void should_render_the_create_form() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var response = httpClient.get("/notifications/new");

                assertThat(response.code()).isEqualTo(200);
                assertThat(response.body().string())
                        .contains("New notification")
                        .contains("Create notification")
                        .contains("<select")
                        .contains("name=\"type\"")
                        .contains("value=\"email\"");
            });
        }

        @Test
        void should_create_an_email_notification_and_return_to_the_list() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(notificationGateway.getAll()).thenReturn(emptyList());
                final var requestCaptor = ArgumentCaptor.forClass(AddNotificationRequest.class);

                final var response = httpClient.post(
                        "/notifications", "value=ada%40example.com");

                verify(notificationGateway).add(requestCaptor.capture());
                assertThat(requestCaptor.getValue())
                        .extracting(AddNotificationRequest::type, AddNotificationRequest::value)
                        .containsExactly("email", "ada@example.com");
                assertThat(response.code()).isEqualTo(200);
                assertThat(response.request().url().encodedPath()).isEqualTo("/notifications");
            });
        }

        @Test
        void should_validate_and_preserve_an_invalid_email_address() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var response = httpClient.post(
                        "/notifications", "value=invalid-email");

                assertThat(response.code()).isEqualTo(400);
                assertThat(response.body().string())
                        .contains("Enter a valid email address.")
                        .contains("value=\"invalid-email\"");
            });
        }
    }

    @Nested
    class NotificationDetailsPage {

        @Test
        void should_render_page() {
            JavalinTest.test(server, (server, httpClient) -> {
                final var notification = new NotificationResponse("1", "email", "ada@example.com");
                when(notificationGateway.get("1")).thenReturn(notification);

                final var response = httpClient.get("/notifications/1");

                assertThat(response.header("Content-Type")).isEqualTo("text/html");
                assertThat(response.body().string())
                        .contains(notification.type())
                        .contains(notification.value())
                        .contains("Delete notification");
                assertThat(response.code()).isEqualTo(200);
            });
        }

        @Test
        void should_render_not_found_for_an_invalid_notification_id() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(notificationGateway.get("72"))
                        .thenThrow(new ResourceNotFoundException("not found"));

                final var response = httpClient.get("/notifications/72");

                assertThat(response.body().string()).contains("Page not found");
                assertThat(response.code()).isEqualTo(404);
            });
        }

        @Test
        void should_delete_notification_and_return_to_notifications() {
            JavalinTest.test(server, (server, httpClient) -> {
                when(notificationGateway.getAll()).thenReturn(emptyList());

                final var response = httpClient.post("/notifications/1/delete");

                verify(notificationGateway).remove(new RemoveNotificationRequest("1"));
                assertThat(response.body().string()).contains("No notifications configured");
                assertThat(response.code()).isEqualTo(200);
            });
        }
    }
}
