package org.gbl.controller;

public record CreateContactForm(String name, String birthdate) {

    public CreateContactForm {
        name = valueOrEmpty(name);
        birthdate = valueOrEmpty(birthdate);
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
