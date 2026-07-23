package org.gbl.form;

public record CreateContactForm(String name, String birthdate) {

    public CreateContactForm {
        name = valueOrEmpty(name);
        birthdate = valueOrEmpty(birthdate);
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
