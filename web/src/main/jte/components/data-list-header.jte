@param String title
@param String headingId
@param String description
@param int total
@param String itemName
@param String actionHref
@param String actionLabel

<div class="data-list__header">
    <div>
        <h2 id="${headingId}">${title}</h2>
        @if(description != null && !description.isBlank())
            <p class="data-list__description">${description}</p>
        @endif
    </div>
    <div class="data-list__header-actions">
        <span class="data-list__count">
            ${total} ${itemName}${total == 1 ? "" : "s"}
        </span>
        @if(actionHref != null && actionLabel != null)
            <a class="btn btn--primary" href="${actionHref}">${actionLabel}</a>
        @endif
    </div>
</div>
