@import org.gbl.view.contacts.CreateContactViewModel

@param CreateContactViewModel viewModel

@template.layout.layout(title = "Add contact | Birthday Reminder")

@template.layout.header(
    title = "Add contact",
    subtitle = "Save a birthday you want to remember.",
    activeSection = "contacts")

<main class="container">
    <div class="detail-view__back">
        <a class="text-link" href="/contacts">← Back to contacts</a>
    </div>

    <section class="card detail-view" aria-labelledby="create-contact-heading">
        <div class="card__header detail-view__header">
            <h2 id="create-contact-heading">New contact</h2>
        </div>

        <form action="/contacts" method="post">
            <div class="detail-view__fields">
                <div class="detail-view__field detail-view__field--full">
                    <label class="detail-view__label" for="contact-name">Name</label>
                    <input class="detail-view__input ${viewModel.nameError() != null ? "detail-view__input--error" : ""}"
                           id="contact-name"
                           name="name"
                           type="text"
                           value="${viewModel.name()}"
                           autocomplete="name"
                           required
                           autofocus
                           aria-describedby="${viewModel.nameError() != null ? "contact-name-error" : ""}"
                           aria-invalid="${viewModel.nameError() != null}">
                    @if(viewModel.nameError() != null)
                        <p class="field-error" id="contact-name-error">${viewModel.nameError()}</p>
                    @endif
                </div>

                <div class="detail-view__field detail-view__field--full">
                    <label class="detail-view__label" for="contact-birthday">Birthday</label>
                    <input class="detail-view__input ${viewModel.birthdateError() != null ? "detail-view__input--error" : ""}"
                           id="contact-birthday"
                           name="birthdate"
                           type="date"
                           value="${viewModel.birthdate()}"
                           required
                           aria-describedby="${viewModel.birthdateError() != null ? "contact-birthday-error" : ""}"
                           aria-invalid="${viewModel.birthdateError() != null}">
                    @if(viewModel.birthdateError() != null)
                        <p class="field-error" id="contact-birthday-error">${viewModel.birthdateError()}</p>
                    @endif
                </div>
            </div>

            <div class="detail-view__actions">
                <a class="btn btn--secondary" href="/contacts">Cancel</a>
                <button class="btn btn--primary" type="submit">Create contact</button>
            </div>
        </form>
    </section>
</main>

@template.layout.footer()
