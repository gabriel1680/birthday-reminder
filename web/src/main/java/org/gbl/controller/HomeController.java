package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.presenter.UpcomingBirthdaysPresenter;
import org.gbl.service.ContactsService;

import java.time.ZoneId;

import static org.gbl.config.JTEPages.HOME_PAGE;

public class HomeController extends JavalinController {

    private static final int UPCOMING_BIRTHDAYS_LIMIT = 3;

    private final ContactsService contactsService;
    private final UpcomingBirthdaysPresenter presenter;

    public HomeController(ContactsService contactsService, UpcomingBirthdaysPresenter presenter) {
        this.contactsService = contactsService;
        this.presenter = presenter;
    }

    public void homePage(Context context) {
        final var zoneId = ZoneId.of("America/Sao_Paulo");
        final var size = Math.min(UPCOMING_BIRTHDAYS_LIMIT, Math.max(1, getSizeFrom(context)));
        final var birthdays = contactsService.getUpcomingBirthdays(size, zoneId);
        context.render(HOME_PAGE, toViewModelMap(presenter.toView(birthdays)));
    }

    private static int getSizeFrom(Context context) {
        final var size = context.queryParam("size");
        if (size == null) return UPCOMING_BIRTHDAYS_LIMIT;
        return Integer.parseInt(size);
    }
}
