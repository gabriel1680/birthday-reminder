package org.gbl.contacts.infra;

import org.gbl.contacts.application.service.IdProvider;

import java.util.UUID;

public class UUIDIdProvider implements IdProvider {

    @Override
    public String provideId() {
        return UUID.randomUUID().toString();
    }
}
