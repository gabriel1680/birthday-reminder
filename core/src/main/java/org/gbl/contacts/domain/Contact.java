package org.gbl.contacts.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Contact {
    private String id;
    private String name;
    private LocalDate birthdate;

    public Contact(String id, String name, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public LocalDate birthdate() {
        return birthdate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
