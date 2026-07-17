@param org.gbl.common.search.ContactFilter filter
@param String action

<section class="contact-search" aria-label="Search contacts">
    <form action="${action}" method="get" class="contact-search__form">
        <div class="contact-search__toolbar">
            <label class="visually-hidden" for="name">Search contacts by name</label>
            <input class="contact-search__input"
                   id="name"
                   type="search"
                   name="name"
                   placeholder="Search contacts by name..."
                   value="${filter.name() != null ? filter.name() : ""}">
            <button class="btn btn--primary" type="submit">Search</button>

            <details class="filter-menu">
                <summary class="btn btn--secondary filter-menu__trigger">
                    Filters
                    @if((filter.birthdateFrom() != null && !filter.birthdateFrom().isBlank()) ||
                        (filter.birthdateTo() != null && !filter.birthdateTo().isBlank()))
                        <span class="filter-menu__badge">Active</span>
                    @endif
                </summary>
                <div class="filter-menu__panel">
                    <h2>Birthday range</h2>
                    <div class="filter-menu__fields">
                        <div class="filter-menu__field">
                            <label for="birthdateFrom">From</label>
                            <input id="birthdateFrom"
                                   type="date"
                                   name="birthdateFrom"
                                   value="${filter.birthdateFrom() != null ? filter.birthdateFrom() : ""}">
                        </div>
                        <div class="filter-menu__field">
                            <label for="birthdateTo">To</label>
                            <input id="birthdateTo"
                                   type="date"
                                   name="birthdateTo"
                                   value="${filter.birthdateTo() != null ? filter.birthdateTo() : ""}">
                        </div>
                    </div>
                    <div class="filter-menu__actions">
                        <a href="${action}" class="btn btn--secondary">Clear all</a>
                        <button class="btn btn--primary" type="submit">Apply filters</button>
                    </div>
                </div>
            </details>
        </div>

        @if((filter.birthdateFrom() != null && !filter.birthdateFrom().isBlank()) ||
            (filter.birthdateTo() != null && !filter.birthdateTo().isBlank()))
            <div class="contact-search__active-filters" aria-label="Active filters">
                <span class="filter-chip">
                    Birthday:
                    ${filter.birthdateFrom() != null && !filter.birthdateFrom().isBlank() ? filter.birthdateFrom() : "Any date"}
                    –
                    ${filter.birthdateTo() != null && !filter.birthdateTo().isBlank() ? filter.birthdateTo() : "Any date"}
                </span>
                <a class="contact-search__clear" href="${action}">Clear all</a>
            </div>
        @endif
    </form>
</section>
