package org.gbl.common.search;

public record ContactFilter(String name, String birthdateFrom, String birthdateTo) {

    public static ContactFilter empty() {
        return new ContactFilter("", "", "");
    }
}
