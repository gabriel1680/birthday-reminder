package org.gbl;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record CreateContactRequest(String name, LocalDate birthdate) {
    public CreateContactRequest(
            @JsonProperty(required = true) String name,
            @JsonProperty(required = true) LocalDate birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }
}