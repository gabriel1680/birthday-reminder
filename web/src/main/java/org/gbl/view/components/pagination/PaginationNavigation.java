package org.gbl.view.components.pagination;

public record PaginationNavigation<T>(
        PaginationWindow window,
        T filter,
        UrlBuilder<T> urlBuilder
) {

    public String previousUrl() {
        return urlBuilder.previous(window.currentPage(), filter);
    }

    public String nextUrl() {
        return urlBuilder.next(window.currentPage(), filter);
    }

    public String pageUrl(int page) {
        return urlBuilder.page(page, filter);
    }
}
