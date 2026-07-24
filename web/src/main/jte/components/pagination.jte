@param org.gbl.view.common.PaginationNavigation<?> navigation

<nav class="pagination">

    @if(navigation.window().hasPrevious())
        <a href="${navigation.previousUrl()}">
            ← Previous
        </a>
    @else
        <span class="pagination__disabled">← Previous</span>
    @endif

    @for(org.gbl.view.common.PaginationItem item : navigation.window().items())

        @if(item.ellipsis())
            <span class="pagination__ellipsis">...</span>

        @elseif(item.current())
            <span class="pagination__current">${item.page()}</span>

        @else
            <a href="${navigation.pageUrl(item.page())}">
                ${item.page()}
            </a>
        @endif
    @endfor


    @if(navigation.window().hasNext())
        <a href="${navigation.nextUrl()}">
            Next →
        </a>
    @else
        <span class="pagination__disabled">Next →</span>
    @endif

</nav>
