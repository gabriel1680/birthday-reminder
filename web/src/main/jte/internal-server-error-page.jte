@template.layout.layout(title = "Something went wrong | Birthday Reminder")

@template.layout.header(
    title = "Birthday Reminder",
    subtitle = "We are here to help you get back on track.",
    activeSection = "")

<main class="container error-page">
    <section class="card error-state" aria-labelledby="error-title">
        <div class="error-state__icon" aria-hidden="true">
            <svg viewBox="0 0 48 48">
                <path d="M24 7 43 39H5L24 7Z"></path>
                <path d="M24 18v10"></path>
                <circle cx="24" cy="34" r="1"></circle>
            </svg>
        </div>
        <p class="error-state__code">500 · Internal Server Error</p>
        <h2 id="error-title">We hit an unexpected bump</h2>
        <p class="error-state__message">
            Something went wrong while loading this page. Please try again in a moment or return home.
        </p>
        <a class="btn btn--primary error-state__action" href="/">Go to home</a>
    </section>
</main>

@template.layout.footer()
