package org.gbl.reminder.out.email;

public interface EmailSender {
    void send(SendEmailRequest request);
}
