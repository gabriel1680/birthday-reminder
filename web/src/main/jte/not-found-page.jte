@template.layout.layout(title = "Page not found | Birthday Reminder")

@template.layout.header(
    title = "Birthday Reminder",
    subtitle = "Let’s help you find your way back.",
    activeSection = "")

<main class="container error-page">
    <section class="card error-state" aria-labelledby="error-title">
        <div class="error-state__icon" aria-hidden="true">
            <svg viewBox="0 0 48 48">
                <circle cx="21" cy="21" r="13"></circle>
                <path d="m31 31 10 10"></path>
                <path d="M16 18c1.2-1.5 2.8-2.2 5-2.2 3 0 5 1.7 5 4.2 0 3.5-5 3.2-5 6"></path>
                <circle cx="21" cy="30" r="1"></circle>
            </svg>
        </div>
        <p class="error-state__code">404 · Page not found</p>
        <h2 id="error-title">This page seems to be missing</h2>
        <p class="error-state__message">
            The address may be incorrect, or the page may have moved. You can return home and continue from there.
        </p>
        <a class="btn btn--primary error-state__action" href="/">Go to home</a>
    </section>
</main>

@template.layout.footer()
