@import org.gbl.view.notification.NotificationViewModel
@import java.util.Collection

@param int totalNotifications
@param Collection<NotificationViewModel> notifications

@if(notifications.isEmpty())
    @template.components.empty-state(
    icon = "🔍",
    title = "No notifications configured",
    message = "Add a new notification configuration.")
@else
    <div class="notifications__header">
        <div>
            <h2 id="notifications-heading">Configured notifications</h2>
            <p>Birthday reminders currently sent by the service.</p>
        </div>
        <span class="notifications__count">
            ${totalNotifications} notification${totalNotifications == 1 ? "" : "s"}
        </span>
    </div>
    <div class="notifications__table-wrapper">
        <table class="notifications-table">
            <thead>
            <tr>
                <th scope="col">Delivery type</th>
                <th scope="col">Destination</th>
            </tr>
            </thead>
            <tbody>
            @for(NotificationViewModel notification : notifications)
                <tr>
                    <td class="notifications-table__type">${notification.type()}</td>
                    <td>${notification.value()}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
@endif
