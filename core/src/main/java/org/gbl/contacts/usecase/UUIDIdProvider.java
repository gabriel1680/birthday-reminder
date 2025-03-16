package org.gbl.contacts.usecase;

import org.gbl.shared.IdProvider;

import java.util.UUID;

public class UUIDIdProvider implements IdProvider {

    @Override
    public String provideId() {
        return UUID.randomUUID().toString();
    }
}
