package org.gbl.contacts.application.usecase.upcoming_birthdays;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

class FakeClock extends Clock {

    private final Instant now;

    public FakeClock(Instant now) {
        this.now = now;
    }

    @Override
    public ZoneId getZone() {
        return null;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return null;
    }

    @Override
    public Instant instant() {
        return now;
    }
}
