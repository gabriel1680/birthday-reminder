package org.gbl.config;

public final class Routes {

    public static String contactDetails(String id) {
        return "%s/%s".formatted(contacts(), id);
    }

    public static String contacts() {
        return "/contacts";
    }

    public static String createContact() {
        return "%s/new".formatted(contacts());
    }

    public static String notifications() {
        return "/notifications";
    }

    public static String deleteNotification(String notificationId) {
        return "%s/%s/delete".formatted(notifications(),notificationId);
    }

    public static String createNotification() {
        return "%s/new".formatted(notifications());
    }

    public static String notificationDetails(String notificationId) {
        return "%s/%s".formatted(notifications(),notificationId);
    }

    public static String deleteContact(String contactId) {
        return "%s/%s/delete".formatted(contacts(), contactId);
    }

    public static String editContact(String contactId) {
        return "%s/%s/edit".formatted(contacts(), contactId);
    }
}
