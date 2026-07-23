package org.gbl.config;

public final class Routes {

    public static String notifications() {
        return "/notifications";
    }

    public static String contactDetails(String id) {
        return "%s/%s".formatted(contacts(), id);
    }

    public static String contacts() {
        return "/contacts";
    }
}
