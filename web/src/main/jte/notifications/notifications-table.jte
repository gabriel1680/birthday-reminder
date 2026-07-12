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
    <div class="contacts">
        <div class="contacts__header">
            <h2>Contacts</h2>
            <span class="contacts__count">
            ${totalNotifications} notification${totalNotifications == 1 ? "" : "s"}
        </span>
        </div>
        <table class="contacts-table">
            <thead>
            <tr>
                <th>Type</th>
                <th>Value</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            @for(NotificationViewModel notification : notifications)
                <tr>
                    <td class="contacts-table__name">
                        <a href="/notifications/details/${notification.id()}" class="contacts-table__link">
                            ${notification.type()}
                        </a>
                    </td>
                    <td>${notification.value()}</td>
                    <td class="contacts-table__arrow">
                        ›
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
@endif