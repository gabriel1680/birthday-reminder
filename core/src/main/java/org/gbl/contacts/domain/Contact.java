package org.gbl.contacts.domain;

import java.time.LocalDate;

public record Contact(String name, String number, LocalDate birthdate) {}
