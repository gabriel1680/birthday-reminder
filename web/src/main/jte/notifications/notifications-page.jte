@import org.gbl.view.notification.NotificationsViewModel

@param NotificationsViewModel viewModel

@template.layout.layout(title = "Notifications | Birthday Reminder")

@template.layout.header(
    title = "Notifications",
    subtitle = "Choose where birthday reminders are delivered.",
    activeSection = "notifications")

<main class="container">

    <section class="notifications card" aria-labelledby="notifications-heading">
        @template.notifications.notifications-table(
            totalNotifications = viewModel.total(),
            notifications = viewModel.notifications())
    </section>

</main>
</main>

@template.layout.footer()
