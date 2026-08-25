# Spotify architecture

Milestone 8 will provide a realistic `MockSpotifyController`. A production adapter may use Spotify's authorized Android integration, such as App Remote where permitted, to control an installed official Spotify application.

Premium offline playback is a commercial, licensing, and product-integration dependency. This project does not assume the public SDK permits a custom downloadable client. Spotify downloads remain owned by the official app; MP3cool will not inspect protected storage, reverse engineer private APIs, bypass DRM, or implement unofficial downloading. Browsing or download management may need to hand off gracefully to the official application.

Human decisions are required on commercial eligibility, distribution model, credentials, required official-app UX, and offline expectations before real integration.
