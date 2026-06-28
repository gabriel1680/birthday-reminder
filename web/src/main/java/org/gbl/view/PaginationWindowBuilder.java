package org.gbl.view;

import org.gbl.common.search.Pagination;

import java.util.ArrayList;
import java.util.List;

public class PaginationWindowBuilder {

    public static <T> PaginationWindow from(Pagination<T> p) {
        int current = p.page();
        int last = p.lastPage();
        List<PaginationItem> items = new ArrayList<>();
        // simple case: small number of pages
        if (last <= 7) {
            for (int i = 1; i <= last; i++) {
                items.add(new PaginationItem(i, i == current, false));
            }
            return build(p, items);
        }
        // always show first page
        items.add(new PaginationItem(1, current == 1, false));
        // left ellipsis
        if (current > 3) {
            items.add(new PaginationItem(-1, false, true));
        }
        // middle window
        int start = Math.max(2, current - 1);
        int end = Math.min(last - 1, current + 1);
        for (int i = start; i <= end; i++) {
            items.add(new PaginationItem(i, i == current, false));
        }
        // right ellipsis
        if (current < last - 2) {
            items.add(new PaginationItem(-1, false, true));
        }
        // always show last page
        items.add(new PaginationItem(last, current == last, false));
        return build(p, items);
    }

    private static <T> PaginationWindow build(Pagination<T> p, List<PaginationItem> items) {
        int current = p.page();
        int last = p.lastPage();
        return new PaginationWindow(
                items,
                current,
                last,
                current > 1 ? current - 1 : null,
                current < last ? current + 1 : null,
                current > 1,
                current < last
        );
    }
}