package org.gbl.contacts.infra;

import org.gbl.contacts.domain.Contact;
import org.gbl.contacts.domain.ContactRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryContactRepository implements ContactRepository {

    private final List<Contact> contacts;

    public InMemoryContactRepository(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void add(Contact aContact) {
        contacts.add(aContact);
    }

    @Override
    public void remove(Contact aContact) {
        contacts.remove(aContact);
    }

    @Override
    public Optional<Contact> getById(String anId) {
        return contacts.stream().filter(c -> c.id().equals(anId)).findFirst();
    }

    @Override
    public boolean existsByName(String aName) {
        return contacts.stream().anyMatch(contact -> contact.name().equals(aName));
    }

    @Override
    public void update(Contact aContact) {
        final var idx = contacts.indexOf(aContact);
        if (idx != -1) {
            contacts.add(idx, aContact);
        }
    }

    public int size() {
        return contacts.size();
    }

    public List<Contact> contacts() {
        return Collections.unmodifiableList(contacts);
    }
}
