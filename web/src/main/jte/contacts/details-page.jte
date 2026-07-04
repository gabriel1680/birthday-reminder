@import org.gbl.view.ContactView

@param ContactView viewModel

@template.layout.layout(title = "Birthday Reminder")

@template.layout.header( title = "Contact Details", subtitle = "View contact information.")

<main class="container">
    <section class="card">
        <div class="card__header">
            <h2>${viewModel.name()}</h2>
            <form action="/delete/${viewModel.id()}" method="post">
                <button class="btn btn--danger"
                        type="submit"
                        onclick="return confirm('Delete this contact?')">
                    Delete
                </button>
            </form>
        </div>


        <div class="search-form">
            <div class="form-field">
                <label>ID</label>
                <input type="text" value="${viewModel.id()}" readonly>
            </div>

            <div class="form-field">
                <label>Name</label>
                <input type="text" value="${viewModel.name()}" readonly>
            </div>

            <div class="form-field">
                <label>Birthday</label>
                <input type="date" value="${viewModel.birthdate()}" readonly>
            </div>

            <div class="search-form__actions">
                <a class="btn btn--secondary"
                        href="/">
                    Back
                </a>
                <a class="btn btn--primary" href="/edit/${viewModel.id()}">
                    Edit
                </a>
            </div>
        </div>
    </section>
</main>

@template.layout.footer()
