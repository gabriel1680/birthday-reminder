package org.gbl;

public interface ContactRepository {
    void add(Contact aContact);
    boolean has(String aNumber);
}
