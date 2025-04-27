package org.gbl.in;

import io.vavr.control.Try;
import org.gbl.CLITest;
import org.gbl.out.ContactResponse;
import org.gbl.out.ContactsGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetContactTest extends CLITest {

    @Mock
    private ContactsGateway gateway;

    @Override
    protected void setCommandLine() {
        commandLine = new CommandLine(new GetContact(gateway));
    }

    @Test
    void ok() {
        when(gateway.get(anyString())).thenReturn(Try.success(new ContactResponse("1", "Bella", "13/09/1987")));
        int exitCode = commandLine.execute("1");
        assertThat(exitCode).isEqualTo(0);
        var output = "Contact found => id: 1, name: Bella, birthdate: 13/09/1987\n";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void invalidId() {
        int exitCode = commandLine.execute();
        assertThat(exitCode).isEqualTo(2);
        assertThat(err.toString()).contains("Missing required parameter: '<id>'");
        verify(gateway, never()).get(anyString());
    }

    @Test
    void help() {
        int exitCode = commandLine.execute("--help");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Usage: get [-hV] <id>");
        verify(gateway, never()).get(anyString());
    }
}