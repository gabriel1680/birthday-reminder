package org.gbl.integration;

import org.gbl.CLITest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends CLITest {

    @Test
    void create() {
        int exitCode = commandLine.execute("create", "--name=Gabriel", "--birthdate=12/02" +
                "/1998");
        assertThat(exitCode).isEqualTo(0);
        var output =
                "Create contact called with name: Gabriel and birthdate: 12/02/1998\n";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void get() {
        int exitCode = commandLine.execute("get", "1");
        assertThat(exitCode).isEqualTo(0);
        var output = "Contact => id: 1, name: Bella, birthdate: 13/09/1987";
        assertThat(out.toString()).isEqualTo(output);
    }
}
