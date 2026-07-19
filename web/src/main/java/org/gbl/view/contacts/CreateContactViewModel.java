package org.gbl.view.contacts;

public record CreateContactViewModel(
        String name,
        String birthdate,
        String nameError,
        String birthdateError
) {

    public static CreateContactViewModel empty() {
        return new CreateContactViewModel("", "", null, null);
    }

    public boolean hasErrors() {
        return nameError != null || birthdateError != null;
    }
}
