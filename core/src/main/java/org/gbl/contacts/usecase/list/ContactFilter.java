package org.gbl.contacts.usecase.list;

import java.time.LocalDate;

public record ContactFilter(String name, LocalDate birthdate) {}
