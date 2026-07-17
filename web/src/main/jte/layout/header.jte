@param String title
@param String subtitle
@param String activeSection

<header class="hero">
    <div class="hero__content">
        <div class="hero__top">
            <div class="hero__text">
                <h1>${title}</h1>
                @if(subtitle != null && !subtitle.isBlank())
                    <p>${subtitle}</p>
                @endif
            </div>
            <label class="theme-toggle">
                <input class="theme-toggle__input" type="checkbox" aria-label="Toggle color theme">
                <span class="theme-toggle__track" aria-hidden="true">
                    <span class="theme-toggle__thumb"></span>
                </span>
                <span class="theme-toggle__light">
                    <svg class="theme-toggle__icon" viewBox="0 0 24 24" aria-hidden="true">
                        <circle cx="12" cy="12" r="4"></circle>
                        <path d="M12 2v2M12 20v2M4.93 4.93l1.42 1.42M17.66 17.66l1.41 1.41M2 12h2M20 12h2M4.93 19.07l1.42-1.42M17.66 6.34l1.41-1.41"></path>
                    </svg>
                    Light mode
                </span>
                <span class="theme-toggle__dark">
                    <svg class="theme-toggle__icon" viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M20.35 15.35A9 9 0 0 1 8.65 3.65a9 9 0 1 0 11.7 11.7Z"></path>
                    </svg>
                    Dark mode
                </span>
            </label>
        </div>
        <nav class="hero__tabs" aria-label="Primary navigation">
            <a class="hero__tab ${"home".equals(activeSection) ? "hero__tab--active" : ""}"
               href="/"
               aria-current="${"home".equals(activeSection) ? "page" : "false"}">
                Home
            </a>
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
