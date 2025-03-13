package org.gbl.contacts.domain;

public interface ContactRepository {
    void add(Contact aContact);
    boolean has(String aNumber);
}
