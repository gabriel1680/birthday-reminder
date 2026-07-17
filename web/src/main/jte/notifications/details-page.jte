@import org.gbl.view.notification.NotificationViewModel

@param NotificationViewModel viewModel

@template.layout.layout(title = "Notification details | Birthday Reminder")

@template.layout.header(
    title = "Notification details",
    subtitle = "Review this delivery destination or remove it.")

<main class="container">
    <section class="card">
        <div class="card__header notification-details__header">
            <h2>${viewModel.type()}</h2>
            <form action="/notifications/${viewModel.id()}/delete" method="post">
                <button class="btn btn--danger"
                        type="submit"
                        onclick="return confirm('Delete this notification?')">
                    Delete notification
                </button>
            </form>
        </div>

        <div class="search-form">
            <div class="form-field">
                <label for="notification-id">ID</label>
                <input id="notification-id" type="text" value="${viewModel.id()}" readonly>
            </div>

            <div class="form-field">
                <label for="notification-type">Delivery type</label>
                <input id="notification-type" type="text" value="${viewModel.type()}" readonly>
            </div>

            <div class="form-field form-field--full">
                <label for="notification-destination">Destination</label>
                <input id="notification-destination" type="text" value="${viewModel.value()}" readonly>
            </div>

            <div class="search-form__actions">
                <a class="btn btn--secondary" href="/notifications">Back to notifications</a>
            </div>
        </div>
    </section>
</main>

@template.layout.footer()
