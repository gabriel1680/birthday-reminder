package org.gbl.contacts.application.usecase.list;

import java.time.LocalDate;

public record ContactFilter(String name, LocalDate birthdate) {}
