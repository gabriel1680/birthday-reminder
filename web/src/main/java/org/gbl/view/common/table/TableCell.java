package org.gbl.view.common.table;

public sealed interface TableCell {

    record Text(String value) implements TableCell {}

    record Link(String value, String href) implements TableCell {}
}
