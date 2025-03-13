package org.gbl.contacts.domain;

public interface ContactRepository {
    void add(Contact aContact);

    boolean has(String aNumber);

    void remove(String aNumber);

    Contact get(String aNumber);
}
