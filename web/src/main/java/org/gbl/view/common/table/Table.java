package org.gbl.view.common.table;

import java.util.List;

public record Table(
        List<TableColumn> columns,
        List<TableRow> rows
) {

    public boolean isEmpty() {
        return rows.isEmpty();
    }
}
