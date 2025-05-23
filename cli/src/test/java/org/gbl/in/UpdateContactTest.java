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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateContactTest extends CLITest {

    @Mock
    private ContactsGateway gateway;

    @Override
    protected void setCommandLine() {
        commandLine = new CommandLine(new UpdateContact(gateway));
    }

    @Test
    void ok() {
        when(gateway.update(any())).thenReturn(Try.success(null));
        var args = new String[]{"1", "--name=John", "--birthdate=27/02/1998"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        var output = "Contact updated ✅\n";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void clientError() {
        when(gateway.update(any())).thenReturn(Try.failure(new Throwable("some problem")));
        var args = new String[]{"1", "--name=John", "--birthdate=27/02/1998"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(1);
        var output = "Error: some problem\n";
        assertThat(err.toString()).isEqualTo(output);
    }

    @Test
    void invalidId() {
        int exitCode = commandLine.execute();
        assertThat(exitCode).isEqualTo(2);
        var output = "Missing required parameter: '<id>'";
        assertThat(err.toString()).contains(output);
    }

    @Test
    void invalidPayload() {
        int exitCode = commandLine.execute("1");
        assertThat(exitCode).isEqualTo(2);
        var output = "Error: Missing one of arguments: --name=<name> --birthdate=<birthdate>";
        assertThat(err.toString()).isEqualTo(output);
    }

    @Test
    void help() {
        int exitCode = commandLine.execute("--help");
        assertThat(exitCode).isEqualTo(0);
        var output = "Usage: update [-hV] [[-n=<name>] [-b=<birthdate>]]";
        assertThat(out.toString()).contains(output);
    }
}