@param org.gbl.view.common.PaginationWindow pagination
@param org.gbl.common.search.ContactFilter filter
@param org.gbl.view.contacts.ContactUrlBuilder urlBuilder

<nav class="pagination">

    @if(pagination.hasPrevious())
        <a href="${urlBuilder.previous(pagination.currentPage(), filter)}">
            ← Previous
        </a>
    @else
        <span class="pagination__disabled">← Previous</span>
    @endif

    @for(org.gbl.view.common.PaginationItem item : pagination.items())

        @if(item.ellipsis())
            <span class="pagination__ellipsis">...</span>

        @elseif(item.current())
            <span class="pagination__current">${item.page()}</span>

        @else
            <a href="${urlBuilder.page(item.page(), filter)}">
                ${item.page()}
            </a>
        @endif
    @endfor


    @if(pagination.hasNext())
        <a href="${urlBuilder.next(pagination.currentPage(), filter)}">
            Next →
        </a>
    @else
        <span class="pagination__disabled">Next →</span>
    @endif

</nav>