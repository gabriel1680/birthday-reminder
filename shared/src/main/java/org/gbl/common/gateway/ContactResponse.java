package org.gbl.common.gateway;

import java.time.LocalDate;

public record ContactResponse(String id, String name, LocalDate birthdate) {
}
