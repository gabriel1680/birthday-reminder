package org.gbl.integration;

import org.gbl.CLITest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends CLITest {

    @BeforeAll
    static void beforeAll() {
        System.setProperty("env", "test");
    }

    @Test
    void create() {
        var args = new String[]{"create", "--name=Gabriel", "--birthdate=12/02/1998"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        var output = "Create contact with id: 1, name: Gabriel and birthdate: 12/02/1998\n";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void get() {
        int exitCode = commandLine.execute("get", "1");
        assertThat(exitCode).isEqualTo(0);
        var output = "Contact found => id: 1, name: Bella, birthdate: 13/09/1987\n";
        assertThat(out.toString()).isEqualTo(output);
    }

    @Test
    void update() {
        var args = new String[]{"update", "1", "--name=John", "--birthdate=27/02/1998"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).isEqualTo("Contact updated ✅\n");
    }

    @Test
    void delete() {
        var args = new String[]{"delete", "1"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        assertThat(out.toString()).isEqualTo("Contact deleted ✅\n");
    }

    @Test
    void search() {
        var args = new String[]{"search"};
        int exitCode = commandLine.execute(args);
        assertThat(exitCode).isEqualTo(0);
        String message = """
                Contacts retrieved
                
                _____________________________________________________________________
                id | name | birthdate
                1 | Bella | 13/09/1987
                _____________________________________________________________________
                current_page: 1 | last_page: 1 | total: 1
                """;
        assertThat(out.toString()).isEqualTo(message);
    }
}
