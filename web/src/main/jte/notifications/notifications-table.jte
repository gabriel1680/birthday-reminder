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
    <div class="data-list">
        <div class="data-list__header">
            <div>
                <h2 id="notifications-heading">Configured notifications</h2>
                <p class="data-list__description">Birthday reminders currently sent by the service.</p>
            </div>
            <span class="data-list__count">
                ${totalNotifications} notification${totalNotifications == 1 ? "" : "s"}
            </span>
        </div>
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
                            <a href="/notifications/${notification.id()}" class="data-list__link">
                                ${notification.type()}
                            </a>
                        </td>
                        <td>${notification.value()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
@endif
