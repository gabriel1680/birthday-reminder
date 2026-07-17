@import org.gbl.view.ContactView
@import java.util.Collection

@param int totalContacts
@param Collection<ContactView> contacts

@if(contacts.isEmpty())
    @template.components.empty-state(
        icon = "🔍",
        title = "No contacts found",
        message = "Try adjusting your search filters.")
@else
    <section class="data-list card">
        <div class="data-list__header">
            <h2>Contacts</h2>
            <span class="data-list__count">
                ${totalContacts} contact${totalContacts == 1 ? "" : "s"}
            </span>
        </div>
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
                @for(ContactView contact : contacts)
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
    </section>
@endif
