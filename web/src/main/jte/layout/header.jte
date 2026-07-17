@param String title
@param String subtitle
@param String activeSection

<header class="hero">
    <div class="hero__content">
        <div class="hero__text">
            <h1>${title}</h1>
            @if(subtitle != null && !subtitle.isBlank())
                <p>${subtitle}</p>
            @endif
        </div>
        <nav class="hero__tabs" aria-label="Primary navigation">
            <a class="hero__tab ${"contacts".equals(activeSection) ? "hero__tab--active" : ""}"
               href="/contacts"
               aria-current="${"contacts".equals(activeSection) ? "page" : "false"}">
                Contacts
            </a>
            <a class="hero__tab ${"notifications".equals(activeSection) ? "hero__tab--active" : ""}"
               href="/notifications"
               aria-current="${"notifications".equals(activeSection) ? "page" : "false"}">
                Notifications
            </a>
        </nav>
    </div>
</header>
