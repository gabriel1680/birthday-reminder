@import org.gbl.config.Routes
@import org.gbl.view.contacts.UpcomingBirthday
@import java.util.Collection

@param Collection<UpcomingBirthday> upcomingBirthdays

<section class="card">

    <div class="card__header">
        <h2>Upcoming Birthdays</h2>
    </div>

    @if(upcomingBirthdays.isEmpty())

        @template.components.empty-state(
            icon = "🎉",
            title = "No upcoming birthdays",
            message = "Looks like there are no birthdays coming soon.")

    @else
        <div class="birthday-grid">
            @for(UpcomingBirthday birthday : upcomingBirthdays)
                <a href="${Routes.contactDetails(birthday.contact().id())}"
                   class="birthday-card">
                    <div class="birthday-card__icon"> 🎂 </div>
                    <div class="birthday-card__name"> ${birthday.contact().name()} </div>
                    <div class="birthday-card__date"> ${birthday.contact().birthdate()} </div>
                    <span class="birthday-card__badge"> ${birthday.badge()} </span>
                </a>
            @endfor
        </div>
    @endif

</section>
