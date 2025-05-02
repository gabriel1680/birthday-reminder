package org.gbl.contacts.application.service.query;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationOutputTest {

    @Test
    void empty() {
        final var pagination = PaginationOutput.emptyOf(1, 1);
        assertThat(pagination.total()).isEqualTo(0);
        assertThat(pagination.values()).isEmpty();
    }

    @Test
    void no_elements() {
        final var pagination = PaginationOutput.emptyOf(1, 1);
        assertThat(pagination.lastPage()).isEqualTo(1);
    }

    @Test
    void even_pagination() {
        var values = new ArrayList<>(Collections.nCopies(5, "test"));
        final var pagination = new PaginationOutput<>(1, 5, 20, values);
        assertThat(pagination.lastPage()).isEqualTo(4);
    }

    @Test
    void odd_pagination() {
        var values = new ArrayList<>(Collections.nCopies(5, "test"));
        final var pagination = new PaginationOutput<>(1, 5, 22, values);
        assertThat(pagination.lastPage()).isEqualTo(4);
    }
}