package org.gbl.in;

import io.vavr.control.Try;
import org.gbl.CLITest;
import org.gbl.out.ContactFilter;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import org.gbl.out.Pagination;
import org.gbl.out.SearchRequest;
import org.gbl.out.SortingOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchContactsTest extends CLITest {

    @Mock
    private ContactsGateway gateway;

    @Override
    protected void setCommandLine() {
        commandLine = new CommandLine(new SearchContacts(gateway));
    }

    @Test
    void success() {
        final var pagination = new Pagination<ContactResponse>(1, 5, 1, 1, emptyList());
        when(gateway.search(any())).thenReturn(Try.success(pagination));
        int statusCode = commandLine.execute();
        assertThat(statusCode).isEqualTo(0);
        assertThat(out.toString()).contains("Contacts retrieved");
        assertThat(out.toString()).contains("id | name | birthdate");
        assertThat(out.toString()).contains("current_page: 1 | last_page: 1 | total: 1");
    }

    @Test
    void failure() {
        when(gateway.search(any())).thenReturn(Try.failure(new Throwable("api error")));
        int statusCode = commandLine.execute();
        assertThat(statusCode).isEqualTo(1);
        assertThat(err.toString()).contains("Error on search contacts: api error");
    }

    @Captor
    private ArgumentCaptor<SearchRequest<ContactFilter>> requestArgumentCaptor;

    @Test
    void defaultSearchValues() {
        final var pagination = new Pagination<ContactResponse>(1, 5, 1, 1, emptyList());
        when(gateway.search(any())).thenReturn(Try.success(pagination));
        int statusCode = commandLine.execute();
        assertThat(statusCode).isEqualTo(0);
        verify(gateway).search(requestArgumentCaptor.capture());
        var searchRequest = requestArgumentCaptor.getValue();
        assertThat(searchRequest)
                .isNotNull()
                .extracting(SearchRequest::page, SearchRequest::size, SearchRequest::order,
                            SearchRequest::filter)
                .containsExactly(1, 15, SortingOrder.ASC, null);
    }

    @ParameterizedTest
    @MethodSource("searchWithParamsProvider")
    void searchWithParams(String[] args) {
        final var pagination = new Pagination<ContactResponse>(1, 5, 1, 1, emptyList());
        when(gateway.search(any())).thenReturn(Try.success(pagination));
        int statusCode = commandLine.execute(args);
        assertThat(statusCode).isEqualTo(0);
        verify(gateway).search(requestArgumentCaptor.capture());
        var searchRequest = requestArgumentCaptor.getValue();
        assertThat(searchRequest)
                .isNotNull()
                .extracting(SearchRequest::page, SearchRequest::size, SearchRequest::order,
                            SearchRequest::filter)
                .containsExactly(2, 9, SortingOrder.DESC, null);
    }

    private static Stream<Arguments> searchWithParamsProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{"--page=2", "--size=9", "--order=desc"}),
                Arguments.of((Object) new String[]{"-p", "2", "-s", "9", "-o", "desc"}));
    }
}