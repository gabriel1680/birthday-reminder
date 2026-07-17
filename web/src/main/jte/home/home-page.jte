@import org.gbl.view.HomeViewModel

@param HomeViewModel viewModel

@template.layout.layout(title = "Birthday Reminder")

@template.layout.header(
    title = "🎂 Birthday Reminder",
    subtitle = "Never miss an important birthday again.",
    activeSection = "home")

<main class="container">
    @template.contacts.upcoming-birthdays(upcomingBirthdays = viewModel.upcomingBirthdays())
</main>

@template.layout.footer()
