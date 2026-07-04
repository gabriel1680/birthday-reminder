@import org.gbl.view.UpcomingBirthday
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
                <a href="/contacts/${birthday.id()}"
                   class="birthday-card">
                    <div class="birthday-card__icon"> 🎂 </div>
                    <div class="birthday-card__name"> ${birthday.name()} </div>
                    <div class="birthday-card__date"> ${birthday.birthdate()} </div>
                    <span class="birthday-card__badge"> ${birthday.badge()} </span>
                </a>
            @endfor
        </div>
    @endif

</section>