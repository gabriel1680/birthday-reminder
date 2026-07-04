package org.gbl.common.gateway;

import java.time.LocalDate;

public record CreateContactRequest(String name, LocalDate birthdate) {
}
