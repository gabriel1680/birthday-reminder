package org.gbl.view;

import org.gbl.view.contacts.UpcomingBirthday;

import java.util.Collection;

public record HomeViewModel(Collection<UpcomingBirthday> upcomingBirthdays) {
}
