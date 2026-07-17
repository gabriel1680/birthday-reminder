@import org.gbl.view.notification.NotificationViewModel

@param NotificationViewModel viewModel

@template.layout.layout(title = "Notification details | Birthday Reminder")

@template.layout.header(
    title = "Notification details",
    subtitle = "Review this delivery destination or remove it.")

<main class="container">
    <section class="card detail-view">
        <div class="card__header detail-view__header">
            <h2>${viewModel.type()}</h2>
            <form action="/notifications/${viewModel.id()}/delete" method="post">
                <button class="btn btn--danger"
                        type="submit"
                        onclick="return confirm('Delete this notification?')">
                    Delete notification
                </button>
            </form>
        </div>

        <div class="detail-view__fields">
            <div class="detail-view__field">
                <label class="detail-view__label" for="notification-id">ID</label>
                <input class="detail-view__input" id="notification-id" type="text" value="${viewModel.id()}" readonly>
            </div>

            <div class="detail-view__field">
                <label class="detail-view__label" for="notification-type">Delivery type</label>
                <input class="detail-view__input" id="notification-type" type="text" value="${viewModel.type()}" readonly>
            </div>

            <div class="detail-view__field detail-view__field--full">
                <label class="detail-view__label" for="notification-destination">Destination</label>
                <input class="detail-view__input" id="notification-destination" type="text" value="${viewModel.value()}" readonly>
            </div>

            <div class="detail-view__actions">
                <a class="btn btn--secondary" href="/notifications">Back to notifications</a>
            </div>
        </div>
    </section>
</main>

@template.layout.footer()
