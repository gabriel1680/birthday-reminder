@import org.gbl.view.contacts.ContactViewModel
@import java.util.Collection

@param int totalContacts
@param Collection<ContactViewModel> contacts

<section class="data-list card" aria-labelledby="contacts-heading">
    @template.components.data-list-header(
        title = "Contacts",
        headingId = "contacts-heading",
        description = null,
        total = totalContacts,
        itemName = "contact",
        actionHref = "/contacts/new",
        actionLabel = "+ Add contact")

    @if(contacts.isEmpty())
        @template.components.empty-state(
            icon = "🔍",
            title = "No contacts found",
            message = "Try adjusting your search filters or add a new contact.")
    @else
        <div class="data-list__table-wrapper">
            <table class="data-list__table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Birthday</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                @for(ContactViewModel contact : contacts)
                    <tr>
                        <td class="data-list__primary">
                            <a href="/contacts/${contact.id()}" class="data-list__link">
                                ${contact.name()}
                            </a>
                        </td>
                        <td>${contact.birthdate()}</td>
                        <td class="data-list__arrow">
                            ›
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    @endif
</section>
