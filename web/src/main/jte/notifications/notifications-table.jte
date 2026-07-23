@import org.gbl.config.Routes
@import org.gbl.view.notification.NotificationViewModel
@import java.util.Collection

@param int totalNotifications
@param Collection<NotificationViewModel> notifications

<div class="data-list">
    @template.components.data-list-header(
        title = "Configured notifications",
        headingId = "notifications-heading",
        description = "Birthday reminders currently sent by the service.",
        total = totalNotifications,
        itemName = "notification",
        actionHref = Routes.createNotification(),
        actionLabel = "+ Add notification")

    @if(notifications.isEmpty())
        @template.components.empty-state(
            icon = "🔍",
            title = "No notifications configured",
            message = "Add a new notification configuration.")
    @else
        <div class="data-list__table-wrapper">
            <table class="data-list__table">
                <thead>
                <tr>
                    <th scope="col">Delivery type</th>
                    <th scope="col">Destination</th>
                </tr>
                </thead>
                <tbody>
                @for(NotificationViewModel notification : notifications)
                    <tr>
                        <td class="data-list__type">
                            <a href="${Routes.notificationDetails(notification.id())}" class="data-list__link">
                                ${notification.type()}
                            </a>
                        </td>
                        <td>${notification.value()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    @endif
</div>
