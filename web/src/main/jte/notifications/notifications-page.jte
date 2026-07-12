@import org.gbl.view.notification.NotificationsViewModel

@param NotificationsViewModel viewModel

@template.layout.layout(title = "Birthday Reminder")

@template.layout.header( title = "Notifications", subtitle = "Never miss an important birthday again.")

<main class="container">

    @template.notifications.notifications-table(
    totalNotifications = viewModel.total(),
    notifications = viewModel.notifications())

</main>

@template.layout.footer()
