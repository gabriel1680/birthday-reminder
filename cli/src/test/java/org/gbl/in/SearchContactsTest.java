package org.gbl.in;

import io.vavr.control.Try;
import org.gbl.CLITest;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.common.search.SearchRequest;
import org.gbl.common.search.SortingOrder;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
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

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchContactsTest extends CLITest {

    private final Pagination<ContactResponse> emptyPagination = new Pagination<>(1, 5, 0, 1, emptyList());

    @Mock
    private ContactsGateway gateway;

    @Override
    protected void setCommandLine() {
        commandLine = new CommandLine(new SearchContacts(gateway));
    }

    @Test
    void success() {
        when(gateway.search(any())).thenReturn(Try.success(emptyPagination));
        int statusCode = commandLine.execute();
        assertThat(statusCode).isEqualTo(0);
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
        when(gateway.search(any())).thenReturn(Try.success(emptyPagination));
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
        when(gateway.search(any())).thenReturn(Try.success(emptyPagination));
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

    @Test
    void tableRender() {
        final var johnDoe = new ContactResponse("85527e63-86f2-4ea8-8cea-7112b0a792e7", "John Doe", "1990-09-10");
        final var maryAnn = new ContactResponse("85527e63-86f2-4ea8-8cea-7112b0a792e8", "Mary Ann", "1991-09-10");
        final var pagination = new Pagination<>(1, 5, 2, 1, List.of(johnDoe, maryAnn));
        when(gateway.search(any())).thenReturn(Try.success(pagination));
        commandLine.execute();
        final var expected = """
                Contacts retrieved
                                                                    
                _____________________________________________________________________
                id | name | birthdate
                85527e63-86f2-4ea8-8cea-7112b0a792e7 | John Doe | 1990-09-10
                85527e63-86f2-4ea8-8cea-7112b0a792e8 | Mary Ann | 1991-09-10
                _____________________________________________________________________
                current_page: 1 | last_page: 1 | total: 2
                 """;
        assertThat(out.toString()).isEqualTo(expected);
    }
}