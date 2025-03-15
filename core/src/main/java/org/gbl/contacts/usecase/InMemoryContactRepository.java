package org.gbl.contacts.usecase;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryContactRepository implements ContactRepository {

    private final Map<String, Contact> contacts = new HashMap<>();

    public InMemoryContactRepository(List<Contact> contacts) {
        contacts.forEach(contact -> this.contacts.put(contact.id(), contact));
    }

    @Override
    public void add(Contact aContact) {
        contacts.put(aContact.id(), aContact);
    }

    @Override
    public void remove(String anId) {
        contacts.remove(anId, contacts.get(anId));
    }

    @Override
    public Optional<Contact> getById(String anId) {
        return Optional.ofNullable(contacts.get(anId));
    }

    @Override
    public boolean existsByName(String aName) {
        return contacts.values().stream()
                .anyMatch(contact -> contact.name().equals(aName));
    }

    public int size() {
        return contacts.size();
    }
}
