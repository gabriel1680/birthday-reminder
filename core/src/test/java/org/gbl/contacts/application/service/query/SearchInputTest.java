package org.gbl.contacts.application.service.query;

import org.gbl.contacts.application.service.query.SearchInput.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SearchInputTest {

    @Test
    void invalidPage() {
        assertThatThrownBy(() -> new SearchInput<>(-1, 1, SortingOrder.ASC, null))
                .isInstanceOf(InvalidSearchInputException.class)
                .hasMessage("invalid page: value should be greater than 0");
    }

    @Test
    void invalidTake() {
        assertThatThrownBy(() -> new SearchInput<>(1, 0, SortingOrder.ASC, null))
                .isInstanceOf(InvalidSearchInputException.class)
                .hasMessage("invalid size: value should be greater than 0");
    }

    @Test
    void noFilter() {
        var input = new SearchInput<Void>(1, 1, SortingOrder.ASC, null);
        assertThat(input.hasFilter()).isFalse();
    }

    @Test
    void withFilter() {
        var input = new SearchInput<>(1, 1, SortingOrder.ASC, "");
        assertThat(input.hasFilter()).isTrue();
    }

    @Test
    void basicInput() {
        assertThatNoException()
                .isThrownBy(() -> SearchInput.of(1, 1));
    }

    @Test
    void offset_page_1() {
        var input = new SearchInput<>(1, 5, SortingOrder.ASC, null);
        assertThat(input.offset())
                .extracting(Offset::start, Offset::end)
                .containsExactly(0, 5);
    }

    @Test
    void offset_page_2() {
        var input = new SearchInput<>(2, 5, SortingOrder.ASC, null);
        assertThat(input.offset())
                .extracting(Offset::start, Offset::end)
                .containsExactly(5, 10);
    }
}