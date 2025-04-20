package org.gbl.integration;

import org.gbl.CLITest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends CLITest {

    @Test
    void create() {
        var args = new String[]{"create", "--name=Gabriel", "--birthdate=12/02/1998"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        var output = "Create contact with name: Gabriel and birthdate: 12/02/1998";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void get() {
        int exitCode = commandLine.execute("get", "1");
        assertThat(exitCode).isEqualTo(0);
        var output = "Contact => id: 1, name: Bella, birthdate: 13/09/1987";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void update() {
        var args = new String[]{"update", "--name=John", "--birthdate=27/02/1998"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        var output = "Update contact with name: John and birthdate: 27/02/1998";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void delete() {
        var args = new String[]{"delete", "1"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        var output = "Contact of id 1 deleted";
        assertThat(out.toString()).isEqualTo(output);
    }
}
