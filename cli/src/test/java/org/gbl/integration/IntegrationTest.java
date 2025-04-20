package org.gbl.integration;

import org.gbl.CLITest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends CLITest {

    @Nested
    class CreateContact {

        @Test
        void ok() {
            int exitCode = commandLine.execute("create", "--name=Gabriel", "--birthdate=12/02" +
                    "/1998");
            assertThat(exitCode).isEqualTo(0);
            final var createdContactMessage =
                    "Create contact called with name: Gabriel and birthdate: 12/02/1998\n";
            assertThat(out.toString()).isEqualTo(createdContactMessage);
        }

        @ParameterizedTest
        @ValueSource(strings = {"--name=Bruce", "--birthdate=07/04/1975"})
        void invalidPayload(String parameter) {
            assertThat(commandLine.execute("create", parameter)).isEqualTo(2);
            assertThat(err.toString()).containsAnyOf(
                    "Error: Missing required argument(s): --birthdate=<birthdate>",
                    "Error: Missing required argument(s): --name=<name>");
        }
    }

    @Nested
    class GetContact {

        @Test
        void ok() {
            int exitCode = commandLine.execute("get", "1");
            assertThat(exitCode).isEqualTo(0);
            assertThat(out.toString()).isEqualTo("Contact => id: 1, name: Bella, birthdate: " +
                                                         "13/09/1987");
        }

        @Test
        void invalidId() {
            int exitCode = commandLine.execute("get");
            assertThat(exitCode).isEqualTo(2);
            assertThat(err.toString()).contains("Missing required parameter: '<id>'");
        }
    }
}
