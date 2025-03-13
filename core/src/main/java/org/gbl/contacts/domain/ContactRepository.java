package org.gbl.contacts.domain;

import java.util.Optional;

public interface ContactRepository {
    void add(Contact aContact);

    void remove(String aNumber);

    Optional<Contact> get(String aNumber);
}
