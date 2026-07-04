package org.gbl.view;

public record UpcomingBirthday(
        String id,
        String name,
        String birthdate,
        int daysUntilBirthday
) {

    public String badge() {
        if (daysUntilBirthday == 0)
            return "Today 🎂";

        if (daysUntilBirthday == 1)
            return "Tomorrow";

        return "In " + daysUntilBirthday + " days";
    }
}