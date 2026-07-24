@import org.gbl.config.Routes
@import org.gbl.view.contacts.ContactViewModel

@param ContactViewModel viewModel

@template.layout.layout(title = "Birthday Reminder")

@template.layout.header(
    title = "Contact Details",
    subtitle = "View contact information.",
    activeSection = "contacts")

<main class="container">
    <div class="detail-view__back">
        <a class="text-link" href="${Routes.contacts()}">← Back to contacts</a>
    </div>

    <section class="card detail-view">
        <div class="card__header detail-view__header">
            <h2>${viewModel.name()}</h2>
        </div>


        <div class="detail-view__fields">
            <div class="detail-view__field">
                <label class="detail-view__label" for="contact-id">ID</label>
                <input class="detail-view__input" id="contact-id" type="text" value="${viewModel.id()}" readonly>
            </div>

            <div class="detail-view__field">
                <label class="detail-view__label" for="contact-name">Name</label>
                <input class="detail-view__input" id="contact-name" type="text" value="${viewModel.name()}" readonly>
            </div>

            <div class="detail-view__field">
                <label class="detail-view__label" for="contact-birthday">Birthday</label>
                <input class="detail-view__input" id="contact-birthday" type="date" value="${viewModel.birthdate()}" readonly>
            </div>

        </div>

        <div class="detail-view__actions">
            <div class="detail-view__record-actions">
                <form action="${Routes.deleteContact(viewModel.id())}" method="post">
                    <button class="btn btn--danger"
                            type="submit"
                            onclick="return confirm('Delete this contact?')">
                        Delete contact
                    </button>
                </form>
                <a class="btn btn--primary" href="${Routes.editContact(viewModel.id())}">Edit contact</a>
            </div>
        </div>
    </section>
</main>

@template.layout.footer()
