@param String title
@param String subtitle

<header class="hero">
    <div class="hero__content">
        <div class="hero__text">
            <h1>${title}</h1>
            @if(subtitle != null && !subtitle.isBlank())
                <p>${subtitle}</p>
            @endif
        </div>
    </div>
</header>