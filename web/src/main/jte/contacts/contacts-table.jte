@import org.gbl.common.gateway.ContactResponse
@import org.gbl.view.PaginationView
@import java.util.Collection

@param int totalContacts
@param Collection<ContactResponse> contacts

@if(contacts.isEmpty())
    @template.components.empty-state(
        icon = "🔍",
        title = "No contacts found",
        message = "Try adjusting your search filters.")
@else
    <div class="contacts">
        <div class="contacts__header">
            <h2>Contacts</h2>
            <span class="contacts__count">
            ${totalContacts} contact${totalContacts == 1 ? "" : "s"}
        </span>
        </div>
        <table class="contacts-table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birthday</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            @for(ContactResponse contact : contacts)
                <tr>
                    <td class="contacts-table__name">
                        <a href="/contacts/${contact.id()}"
                           class="contacts-table__link">
                            ${contact.name()}
                        </a>
                    </td>
                    <td> ${contact.birthdate()} </td>
                    <td class="contacts-table__arrow">›</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
@endif