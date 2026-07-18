@import org.gbl.view.notification.CreateNotificationViewModel

@param CreateNotificationViewModel viewModel

@template.layout.layout(title = "Add notification | Birthday Reminder")

@template.layout.header(
    title = "Add notification",
    subtitle = "Choose where birthday reminders are delivered.",
    activeSection = "notifications")

<main class="container">
    <div class="detail-view__back">
        <a class="text-link" href="/notifications">← Back to notifications</a>
    </div>

    <section class="card detail-view" aria-labelledby="create-notification-heading">
        <div class="card__header detail-view__header">
            <h2 id="create-notification-heading">New notification</h2>
        </div>

        <form action="/notifications" method="post">
            <div class="detail-view__fields">
                <div class="detail-view__field detail-view__field--full">
                    <label class="detail-view__label" for="notification-type">Delivery type</label>
                    <select class="detail-view__input"
                            id="notification-type"
                            name="type">
                        <option value="email">Email</option>
                    </select>
                </div>

                <div class="detail-view__field detail-view__field--full">
                    <label class="detail-view__label" for="notification-value">Email address</label>
                    <input class="detail-view__input ${viewModel.valueError() != null ? "detail-view__input--error" : ""}"
                           id="notification-value"
                           name="value"
                           type="email"
                           value="${viewModel.value()}"
                           autocomplete="email"
                           required
                           autofocus
                           aria-describedby="${viewModel.valueError() != null ? "notification-value-error" : ""}"
                           aria-invalid="${viewModel.valueError() != null}">
                    @if(viewModel.valueError() != null)
                        <p class="field-error" id="notification-value-error">${viewModel.valueError()}</p>
                    @endif
                </div>
            </div>

            <div class="detail-view__actions">
                <a class="btn btn--secondary" href="/notifications">Cancel</a>
                <button class="btn btn--primary" type="submit">Create notification</button>
            </div>
        </form>
    </section>
</main>

@template.layout.footer()
