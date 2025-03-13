package org.gbl.contacts.usecase;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryContactRepository implements ContactRepository {

    private final Map<String, Contact> contacts = new HashMap<>();

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

    @Override
    public void remove(String number) {
        contacts.remove(number, contacts.get(number));
    }

    public int size() {
        return contacts.size();
    }
}
