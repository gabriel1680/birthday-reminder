package org.gbl.contacts.domain;

import java.util.Optional;

public interface ContactRepository {
    void add(Contact aContact);

    void remove(String anId);

    Optional<Contact> getById(String anId);

    boolean existsByName(String aName);

    void update(Contact aContact);
}
