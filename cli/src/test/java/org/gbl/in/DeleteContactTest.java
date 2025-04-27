package org.gbl.in;

import io.vavr.control.Try;
import org.gbl.CLITest;
import org.gbl.out.ContactsGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteContactTest extends CLITest {

    @Mock
    private ContactsGateway gateway;

    @Override
    protected void setCommandLine() {
        commandLine = new CommandLine(new DeleteContact(gateway));
    }

    @Test
    void ok() {
        when(gateway.delete(any())).thenReturn(Try.success(null));
        int exitCode = commandLine.execute("1");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Contact of id 1 deleted\n");
        verify(gateway, times(1)).delete("1");
    }

    @Test
    void clientError() {
        when(gateway.delete(any())).thenReturn(Try.failure(new Throwable("Err")));
        int exitCode = commandLine.execute("1");
        assertThat(exitCode).isEqualTo(1);
        assertThat(err.toString()).contains("Err\n");
        verify(gateway, times(1)).delete("1");
    }

    @Test
    void invalidArgument() {
        int exitCode = commandLine.execute();
        assertThat(exitCode).isEqualTo(2);
        assertThat(err.toString()).contains("Missing required parameter: '<id>'");
        verify(gateway, never()).delete(anyString());
    }

    @Test
    void help() {
        int exitCode = commandLine.execute("--help");
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).contains("Usage: delete [-hV] <id>");
        verify(gateway, never()).delete(anyString());
    }
}