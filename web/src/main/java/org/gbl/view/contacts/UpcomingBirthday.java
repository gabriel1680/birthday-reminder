package org.gbl.view.contacts;

public record UpcomingBirthday(ContactView contact, int daysUntilBirthday) {

    public String badge() {
        if (daysUntilBirthday == 0) return "Today 🎂";

        if (daysUntilBirthday == 1) return "Tomorrow";

        return "In " + daysUntilBirthday + " days";
    }
}