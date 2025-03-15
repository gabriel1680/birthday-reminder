package org.gbl.contacts.domain;

import java.time.LocalDate;

public record Contact(String id, String name, LocalDate birthdate) {}
