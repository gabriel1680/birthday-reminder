package org.gbl.view.common;

public abstract class UrlBuilder<T> {

    private final String baseUrl;

    public UrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String page(int page, T filter) {
        return baseUrl + "?" + query(page, filter);
    }

    public String next(int page, T filter) {
        return page(page + 1, filter);
    }

    public String previous(int page, T filter) {
        return page(page - 1, filter);
    }

    protected abstract String query(int page, T f);
}
