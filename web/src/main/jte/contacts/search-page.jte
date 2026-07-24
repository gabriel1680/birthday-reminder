@import org.gbl.config.Routes
@import org.gbl.view.contacts.SearchViewModel

@param SearchViewModel viewModel

@template.layout.layout(title = "Contacts | Birthday Reminder")

@template.layout.header(
    title = "Contacts",
    subtitle = "Search and manage birthday contacts.",
    activeSection = "contacts")

<main class="container">
    @template.contacts.search-form(
    filter = viewModel.filter(),
    action = Routes.contacts())

    @template.contacts.contacts-table(
    totalContacts = viewModel.pagination().total(),
    contacts = viewModel.pagination().values())

    @template.components.pagination(
    navigation = viewModel.paginationNavigation())
</main>

@template.layout.footer()
