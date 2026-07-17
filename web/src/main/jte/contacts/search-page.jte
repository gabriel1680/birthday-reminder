@import org.gbl.view.SearchViewModel

@param SearchViewModel viewModel

@template.layout.layout(title = "Contacts | Birthday Reminder")

@template.layout.header(
    title = "Contacts",
    subtitle = "Search and manage birthday contacts.",
    activeSection = "contacts")

<main class="container">
    @template.contacts.search-form(
    filter = viewModel.filter(),
    action = "/contacts")

    @template.contacts.contacts-table(
    totalContacts = viewModel.pagination().total(),
    contacts = viewModel.pagination().values())

    @template.contacts.pagination(
    pagination = viewModel.pagination().window(),
    filter = viewModel.filter(),
    urlBuilder = viewModel.urlBuilder())
</main>

@template.layout.footer()
