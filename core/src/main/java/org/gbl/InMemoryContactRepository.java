package org.gbl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryContactRepository implements ContactRepository {

    private final Map<String, Contact> contacts = new HashMap<>();

    public InMemoryContactRepository() {
    }

    public InMemoryContactRepository(List<Contact> contacts) {
        contacts.forEach(contact -> this.contacts.put(contact.number(), contact));
    }

    @Override
    public void add(Contact aContact) {
        contacts.put(aContact.number(), aContact);
    }

    @Override
    public boolean has(String aNumber) {
        return contacts.containsKey(aNumber);
    }

    public int size() {
        return contacts.size();
    }
}
