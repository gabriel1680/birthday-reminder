package org.gbl.view.presenter;

import org.gbl.common.gateway.ContactResponse;
import org.gbl.common.search.ContactFilter;
import org.gbl.common.search.Pagination;
import org.gbl.view.components.pagination.PaginationNavigation;
import org.gbl.view.components.pagination.PaginationView;
import org.gbl.view.components.pagination.PaginationWindowBuilder;
import org.gbl.view.components.table.Table;
import org.gbl.view.components.table.TableCell;
import org.gbl.view.components.table.TableColumn;
import org.gbl.view.components.table.TableRow;
import org.gbl.view.contacts.ContactUrlBuilder;
import org.gbl.view.contacts.ContactViewModel;
import org.gbl.view.contacts.SearchViewModel;

import java.util.List;

import static org.gbl.config.Routes.contactDetails;

public class ContactSearchPresenter {

    private static final List<TableColumn> TABLE_COLUMNS = List.of(
            new TableColumn("Name"),
            new TableColumn("Birthday"),
            new TableColumn(""));

    private static final TableCell ARROW_CELL = new TableCell.Text("›");

    private final ContactsPresenter contactsPresenter;

    public ContactSearchPresenter() {
        contactsPresenter = new ContactsPresenter();
    }

    public SearchViewModel toView(
            Pagination<ContactResponse> pagination,
            ContactFilter filter
    ) {
        final var window = PaginationWindowBuilder.from(pagination);
        final var contactViews = pagination.values().stream()
                .map(contactsPresenter::toView)
                .toList();
        final var paginationView = new PaginationView<>(
                window,
                pagination.total(),
                contactViews);
        final var navigation = new PaginationNavigation<>(
                window,
                filter,
                new ContactUrlBuilder("/contacts"));
        return new SearchViewModel(paginationView, filter, navigation, toTable(contactViews));
    }

    private static Table toTable(List<ContactViewModel> contacts) {
        final var rows = contacts.stream()
                .map(ContactSearchPresenter::tableRow)
                .toList();
        return new Table(TABLE_COLUMNS, rows);
    }

    private static TableRow tableRow(ContactViewModel contact) {
        final var nameCell = new TableCell.Link(
                contact.name(),
                contactDetails(contact.id()));
        final var birthdateCell = new TableCell.Text(contact.birthdate());
        final List<TableCell> cells = List.of(nameCell, birthdateCell, ARROW_CELL);
        return new TableRow(cells);
    }
}
