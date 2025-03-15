package org.gbl.reminder.app;

import org.gbl.reminder.out.email.EmailSender;
import org.gbl.reminder.out.email.SendEmailRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpyEmailSender implements EmailSender {

    private final List<SendEmailRequest> mailsSent = new ArrayList<>();

    @Override
    public void send(SendEmailRequest request) {
        mailsSent.add(request);
    }

    public List<SendEmailRequest> mailsSent() {
        return Collections.unmodifiableList(mailsSent);
    }
}
