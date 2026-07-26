@import org.gbl.config.Routes
@import org.gbl.view.common.table.Table

@param int totalContacts
@param Table table

<section class="data-list card" aria-labelledby="contacts-heading">
    @template.components.data-list-header(
        title = "Contacts",
        headingId = "contacts-heading",
        description = null,
        total = totalContacts,
        itemName = "contact",
        actionHref = Routes.createContact(),
        actionLabel = "+ Add contact")

    @if(table.isEmpty())
        @template.components.empty-state(
            icon = "🔍",
            title = "No contacts found",
            message = "Try adjusting your search filters or add a new contact.")
    @else
        @template.components.table(table = table)
    @endif
</section>
