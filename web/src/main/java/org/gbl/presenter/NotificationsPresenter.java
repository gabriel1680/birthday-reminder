package org.gbl.presenter;

import org.gbl.common.notification.NotificationResponse;
import org.gbl.view.common.table.Table;
import org.gbl.view.common.table.TableCell;
import org.gbl.view.common.table.TableColumn;
import org.gbl.view.common.table.TableRow;
import org.gbl.view.notification.CreateNotificationViewModel;
import org.gbl.view.notification.NotificationViewModel;
import org.gbl.view.notification.NotificationsViewModel;

import java.util.List;

import static org.gbl.config.Routes.notificationDetails;

public class NotificationsPresenter {

    private static final List<TableColumn> TABLE_COLUMNS =
            List.of(new TableColumn("Delivery type"), new TableColumn("Destination"));

    public NotificationsViewModel toNotificationsList(
            List<NotificationResponse> notificationResponses) {
        final var notifications = notificationResponses.stream()
                .map(this::toNotification)
                .toList();
        return new NotificationsViewModel(notifications.size(), toTable(notifications));
    }

    public NotificationViewModel toNotification(NotificationResponse it) {
        return new NotificationViewModel(it.id(), it.type(), it.value());
    }

    public CreateNotificationViewModel toNotificationError(String value, String valueError) {
        return new CreateNotificationViewModel(value, valueError);
    }

    public CreateNotificationViewModel toNotificationEmpty() {
        return CreateNotificationViewModel.empty();
    }

    private static Table toTable(List<NotificationViewModel> notifications) {
        final var rows = notifications.stream()
                .map(NotificationsPresenter::tableRow)
                .toList();
        return new Table(TABLE_COLUMNS, rows);
    }

    private static TableRow tableRow(NotificationViewModel notification) {
        final var href = notificationDetails(notification.id());
        final var typeCell = new TableCell.Link(notification.type(), href);
        final var valueCell = new TableCell.Text(notification.value());
        final List<TableCell> cells = List.of(typeCell, valueCell);
        return new TableRow(cells);
    }
}
