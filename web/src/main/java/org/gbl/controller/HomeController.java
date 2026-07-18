package org.gbl.controller;

import io.javalin.http.Context;
import org.gbl.common.gateway.ContactsGateway;
import org.gbl.common.gateway.GetUpcomingBirthdaysRequest;
import org.gbl.view.HomeViewModel;
import org.gbl.view.contacts.UpcomingBirthdaysPresenter;

import java.time.ZoneId;
import java.util.Map;

public class HomeController {

    private static final int UPCOMING_BIRTHDAYS_LIMIT = 3;

    private final ContactsGateway contactsGateway;
    private final UpcomingBirthdaysPresenter presenter;

    public HomeController(ContactsGateway contactsGateway, UpcomingBirthdaysPresenter presenter) {
        this.contactsGateway = contactsGateway;
        this.presenter = presenter;
    }

    public void homePage(Context context) {
        final var request = new GetUpcomingBirthdaysRequest(
                UPCOMING_BIRTHDAYS_LIMIT,
                ZoneId.of("America/Sao_Paulo"));
        final var birthdays = contactsGateway.getUpcomingBirthdays(request).get();
        final var viewModel = new HomeViewModel(presenter.toView(birthdays));
        context.render("home/home-page.jte", Map.of("viewModel", viewModel));
    }
}
