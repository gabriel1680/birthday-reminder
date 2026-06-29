package org.gbl.contacts.application.usecase.upcoming_birthdays;

import java.time.ZoneId;

public record GetUpcomingBirthdaysInput(int size, ZoneId zoneId) {
}
