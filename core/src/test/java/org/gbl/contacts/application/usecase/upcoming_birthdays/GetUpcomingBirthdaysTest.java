package org.gbl.contacts.application.usecase.upcoming_birthdays;

import org.gbl.contacts.application.service.query.ContactQueryRepository;
import org.gbl.contacts.application.usecase.shared.ContactOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUpcomingBirthdaysTest {

    private static final Instant NOW =Instant.parse("2020-12-12T00:00:00Z");

    @Mock
    private ContactQueryRepository queryRepository;
    private GetUpcomingBirthdays sut;
    private FakeClock clock;

    @Captor
    private ArgumentCaptor<LocalDate> localDateCaptor;


    @BeforeEach
    void setUp() {
        clock = new FakeClock(NOW);
        sut = new GetUpcomingBirthdays(clock, queryRepository);
    }

    @Test
    void parse_todays_date_with_zoneId_input_offset() {
        final List<ContactOutput> contactsOutput = emptyList();
        when(queryRepository.upcomingBirthdaysFor(any(), anyInt())).thenReturn(contactsOutput);
        final var input = new GetUpcomingBirthdaysInput(1, ZoneId.of("America/Bogota"));
        final var output = sut.execute(input);
        assertThat(output).isEqualTo(contactsOutput);
        verify(queryRepository, times(1)).upcomingBirthdaysFor(localDateCaptor.capture(), eq(1));
        assertThat(localDateCaptor.getValue().toString()).isEqualTo("2020-12-11");
    }
}