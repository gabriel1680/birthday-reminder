@param org.gbl.common.search.ContactFilter filter
@param String action

<section class="card">

    <div class="card__header">
        <h2>Search Contacts</h2>
    </div>

    <form action="${action}" method="get" class="search-form">
        <div class="form-field form-field--full">
            <label for="name"> Name </label>
            <input id="name"
                   type="text"
                   name="name"
                   placeholder="Search by name..."
                   value="${filter.name()}">
        </div>
        <div class="form-field">
            <label for="birthdateFrom">
                Birthday From
            </label>
            <input id="birthdateFrom"
                   type="date"
                   name="birthdateFrom"
                   value="${filter.birthdateFrom() != null ? filter.birthdateFrom() : ""}">
        </div>

        <div class="form-field">
            <label for="birthdateTo"> Birthday To </label>
            <input id="birthdateTo"
                   type="date"
                   name="birthdateTo"
                   value="${filter.birthdateTo() != null ? filter.birthdateTo() : ""}">
        </div>

        <div class="search-form__actions">
            <button class="btn btn--primary" type="submit"> Search </button>
            <a href="${action}" class="btn btn--secondary"> Clear </a>
        </div>

    </form>

</section>>