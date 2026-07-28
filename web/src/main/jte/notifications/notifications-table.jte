@import org.gbl.config.Routes
@import org.gbl.view.components.table.Table

@param int totalNotifications
@param Table table

<div class="data-list">
    @template.components.data-list-header(
        title = "Configured notifications",
        headingId = "notifications-heading",
        description = "Birthday reminders currently sent by the service.",
        total = totalNotifications,
        itemName = "notification",
        actionHref = Routes.createNotification(),
        actionLabel = "+ Add notification")

    @if(table.isEmpty())
        @template.components.empty-state(
            icon = "🔍",
            title = "No notifications configured",
            message = "Add a new notification configuration.")
    @else
        @template.components.table(table = table)
    @endif
</div>
