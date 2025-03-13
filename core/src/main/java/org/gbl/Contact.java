package org.gbl;

import java.time.LocalDate;

public record Contact(String name, String number, LocalDate birthdate) {}
