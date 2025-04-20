package org.gbl.in;

import org.gbl.CLITest;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateContactTest extends CLITest {

    @Mock
    private ContactsGateway gateway;

    @Override
    protected void setCommandLine() {
        commandLine = new CommandLine(new CreateContact(gateway));
    }

    @Test
    void ok() {
        when(gateway.create(any())).thenReturn(new ContactResponse("1", "Gabriel", "12/02/1998"));
        var args = new String[]{"--name=Gabriel", "--birthdate=12/02/1998"};
        var exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        var output =
                "Create contact with name: Gabriel and birthdate: 12/02/1998\n";
        assertThat(out.toString()).isEqualTo(output);
    }

    @ParameterizedTest
    @ValueSource(strings = {"--name=Bruce", "--birthdate=07/04/1975"})
    void invalidPayload(String args) {
        assertThat(commandLine.execute(args)).isEqualTo(2);
        assertThat(err.toString()).containsAnyOf(
                "Error: Missing required argument(s): --birthdate=<birthdate>",
                "Error: Missing required argument(s): --name=<name>");
        verify(gateway, never()).create(any());
    }

    @Test
    void help() {
        int exitCode = commandLine.execute("--help");
        assertThat(exitCode).isEqualTo(0);
        final var helpMessage = "Usage: create [-hV] [-n=<name> -b=<birthdate>]\n";
        assertThat(out.toString()).contains(helpMessage);
        verify(gateway, never()).create(any());
    }
}