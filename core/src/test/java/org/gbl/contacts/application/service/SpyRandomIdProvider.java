package org.gbl.contacts.application.service;

import java.util.Random;

public class SpyRandomIdProvider implements IdProvider {

    private String lastIdProvided;

    @Override
    public String provideId() {
        Random random = new Random(1000L);
        lastIdProvided = random.ints().toString();
        return lastIdProvided;
    }

    public String lastIdProvided() {
        return lastIdProvided;
    }
}
