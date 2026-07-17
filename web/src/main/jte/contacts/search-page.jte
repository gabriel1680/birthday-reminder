@import org.gbl.view.SearchViewModel

@param SearchViewModel viewModel

@template.layout.layout(title = "Birthday Reminder")

@template.layout.header(
    title = "🎂 Birthday Reminder",
    subtitle = "Never miss an important birthday again.",
    activeSection = "contacts")

<main class="container">
    @template.contacts.upcoming-birthdays(upcomingBirthdays = viewModel.upcomingBirthdays())

    @template.contacts.search-form(
    filter = viewModel.filter(),
    action = "/")

    @template.contacts.contacts-table(
    totalContacts = viewModel.pagination().total(),
    contacts = viewModel.pagination().values())

    @template.contacts.pagination(
    pagination = viewModel.pagination().window(),
    filter = viewModel.filter(),
    urlBuilder = viewModel.urlBuilder())
</main>

@template.layout.footer()
