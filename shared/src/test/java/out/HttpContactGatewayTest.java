package out;

import io.vavr.control.Try;
import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.gateway.CreateContactRequest;
import org.gbl.common.gateway.UpdateContactRequest;
import org.gbl.common.gateway.http.HttpApiClient;
import org.gbl.common.gateway.http.HttpContactGateway;
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

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpContactGatewayTest {

    private static final ContactResponse MARY_ANN =
            new ContactResponse("1", "Mary Ann", LocalDate.parse("1959-08-14"));

    private static final ContactResponse JOHN_WICK =
            new ContactResponse("2", "John Wick", LocalDate.parse("1964-09-02"));

    @Mock
    private HttpApiClient httpApiClient;

    private HttpContactGateway sut;

    @BeforeEach
    void setUp() {
        sut = new HttpContactGateway(httpApiClient);
    }

    @Test
    void responseError() {
        final var exception = new RuntimeException("invalid name");
        when(httpApiClient.post(any(), any(), any())).thenReturn(Try.failure(exception));
        var request = new CreateContactRequest("John", LocalDate.parse("1957-04-14"));
        final var output = sut.create(request);
        assertThat(output.isFailure()).isTrue();
        assertThat(output.failed().get())
                .extracting(Throwable::getMessage)
                .isEqualTo(exception.getMessage());
    }

    @Nested
    class WhenCreateAContact {

        private final CreateContactRequest request =
                new CreateContactRequest(MARY_ANN.name(), MARY_ANN.birthdate());

        @Captor
        private ArgumentCaptor<CreateContactRequest> requestCaptor;

        @BeforeEach
        void setUp() {
            when(httpApiClient.post(eq("/contacts"), any(), any())).thenReturn(Try.success(MARY_ANN));
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.create(request).get())
                    .isNotNull()
                    .isSameAs(MARY_ANN);
        }

        @Test
        void shouldCallSendWithAValidRequest() {
            sut.create(request);
            verify(httpApiClient).post(eq("/contacts"), requestCaptor.capture(), any());
            final var httpRequest = requestCaptor.getValue();
            assertThat(httpRequest).extracting(CreateContactRequest::name, CreateContactRequest::birthdate)
                    .containsExactly(request.name(), request.birthdate());
        }
    }

    @Nested
    class WhenGetAContact {

        private final String request = "1";

        @BeforeEach
        void setUp() {
            when(httpApiClient.get("/contacts/1", ContactResponse.class)).thenReturn(Try.success(MARY_ANN));
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.get(request).get()).isSameAs(MARY_ANN);
        }
    }

    @Nested
    class WhenUpdateAContact {

        private final UpdateContactRequest request =
                new UpdateContactRequest(JOHN_WICK.id(), JOHN_WICK.name(), JOHN_WICK.birthdate().toString());

        @BeforeEach
        void setUp() {
            when(httpApiClient.put("/contacts/2", request, ContactResponse.class))
                    .thenReturn(Try.success(JOHN_WICK));
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.update(request).get()).isSameAs(JOHN_WICK);
        }
    }

    @Nested
    class WhenDeleteAContact {

        @Test
        void success() {
            when(httpApiClient.delete("/contacts/1", ContactResponse.class))
                    .thenReturn(Try.success(MARY_ANN));
            final var result = sut.delete("1");
            assertThat(result.isSuccess()).isTrue();
        }
    }

    @Nested
    class WhenSearchContacts {

        private final SearchRequest request = new SearchRequest(1, 1, SortingOrder.ASC, null);

        @BeforeEach
        void setUp() {
            final var pagination = new Pagination<ContactResponse>(1, 1, 1, 1, emptyList());
            when(httpApiClient.get(eq("/contacts?page=1&size=1&order=asc"), any()))
                    .thenReturn(Try.success(pagination));
        }

        @Test
        void shouldReturnAValidOutput() {
            assertThat(sut.search(request).get())
                    .isNotNull()
                    .isInstanceOf(Pagination.class);
        }
    }
}