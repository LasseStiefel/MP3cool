# Dual-Screen MP3 Player UI / UX Design Specification

## 1. Product concept

Design a custom Android-based user interface for a premium portable MP3/music player with two front displays:

- Upper display: color OLED touchscreen.
- Lower display: touch-capable e-paper display.
- The physical device should feel compact, premium, slightly retro and deliberately designed as a dedicated music device rather than a miniature smartphone.
- The operating system is Android underneath, but the user-facing shell should not resemble a standard Android launcher.

The device supports:

- Spotify
- Apple Music
- Local Music
- potentially additional music applications later
- Settings
- Wi-Fi
- Bluetooth audio
- wired audio
- local storage
- downloads
- system search
- playback through Android media sessions

The most important UX principle is:

> The OLED is the content display.  
> The e-paper is the persistent control surface.

The two screens must therefore behave as one coordinated interface.

---

# 2. Design philosophy

The design should feel like a combination of:

- a high-end 2000s dedicated MP3 player
- minimalist industrial equipment
- a modern premium audio product
- a physical hi-fi component
- contemporary typography and interaction design

Do NOT make it look like:

- a normal Android launcher
- a smartphone home screen
- a grid of colorful Android icons
- Material Design
- a generic Spotify clone
- a futuristic cyberpunk interface
- an excessively skeuomorphic retro player

The aesthetic should be:

- minimal
- slightly retro
- functional
- calm
- tactile
- typographic
- music-focused
- premium
- restrained

There should be generous negative space.

Use large typography rather than excessive graphical elements.

Animations on the OLED should be subtle.

The e-paper should intentionally look more graphic and printed.

---

# 3. Core two-screen philosophy

The upper OLED and lower e-paper should not duplicate one another.

Each display has a specific role.

## OLED responsibilities

The OLED is responsible for:

- full applications
- album artwork
- lists
- playlists
- albums
- artist pages
- settings menus
- search results
- rich navigation
- clock / ambient idle screen
- detailed Now Playing interface
- animations
- images
- application UI

## E-paper responsibilities

The e-paper is responsible for:

- application selection
- Home navigation
- playback controls
- current song information
- shortcuts
- contextual controls
- keyboard
- volume controls
- back/home navigation where useful
- sleep-mode information
- persistent playback status

The e-paper is effectively a programmable physical control panel.

---

# 4. Fundamental interaction model

There are four main device states:

1. IDLE / HOME
2. APPLICATION OPEN
3. NOW PLAYING
4. SLEEP

The displays behave differently in each state.

---

# 5. IDLE / HOME state

When the device is awake but no application is currently open:

## OLED

The OLED should NOT show application icons.

Instead it becomes a beautiful ambient home display.

Primary content:

- large clock
- date
- optional small battery indicator
- optional currently playing album art
- extremely restrained status information

Example:

```text
                 14:32

             TUESDAY
             25 AUG


              72%

       CONNECTED • HEADPHONES
```

Alternative when music is playing:

```text
                 14:32


           [ ALBUM ART ]


            NIGHT DRIVE
             Chromatics

               ▶
```

This screen should feel almost like a premium bedside clock or hi-fi display.

It should be visually calm.

---

# 6. E-paper Home / application launcher

The e-paper is the primary launcher.

This is one of the defining interactions of the product.

When no application is open, the e-paper displays music applications and system destinations.

Example:

```text
┌──────────────────────────────┐
│                              │
│  SPOTIFY                     │
│                              │
│  LOCAL MUSIC                 │
│                              │
│  APPLE MUSIC                 │
│                              │
│  SETTINGS                    │
│                              │
└──────────────────────────────┘
```

A slightly richer version:

```text
┌──────────────────────────────┐
│ MUSIC                        │
│ ──────────────────────────── │
│                              │
│ ● SPOTIFY                    │
│                              │
│   LOCAL MUSIC                │
│                              │
│   APPLE MUSIC                │
│                              │
│ ──────────────────────────── │
│   SETTINGS                   │
└──────────────────────────────┘
```

Do not use a conventional 4 × 4 icon grid.

Prefer:

- typography
- simple monochrome icons
- horizontal rows
- large touch targets

The currently selected item may be represented with:

- a filled circle
- thick vertical line
- inverted background
- underline

Avoid animated selection effects because of e-paper refresh characteristics.

---

# 7. Opening an application

Example:

User touches:

SPOTIFY

on the e-paper.

The OLED immediately launches the regular Spotify Android application.

The e-paper does NOT disappear.

Instead it transitions from APP LAUNCHER mode into MEDIA CONTROL mode.

OLED:

```text
┌──────────────────────────────┐
│                              │
│                              │
│       NORMAL SPOTIFY         │
│       ANDROID APP UI         │
│                              │
│                              │
└──────────────────────────────┘
```

E-paper:

```text
┌──────────────────────────────┐
│ NIGHT DRIVE                  │
│ Chromatics                   │
│                              │
│     ◀◀      Ⅱ      ▶▶       │
│                              │
│ ♡      QUEUE       HOME      │
└──────────────────────────────┘
```

This system-level interface should work regardless of whether playback comes from:

- Spotify
- Apple Music
- Local Music
- another compatible Android media application

---

# 8. Android media integration

Implement the e-paper playback interface as a system-level component.

Do NOT require individual applications to implement a custom e-paper UI.

Use Android media APIs such as:

- MediaSession
- MediaController
- MediaMetadata
- PlaybackState

The system should identify the active media session and retrieve:

- song title
- artist
- album
- album artwork
- play / pause state
- previous
- next
- duration where available
- position where appropriate
- active application

The e-paper renderer should convert that information into the device's common playback UI.

This means Spotify, Apple Music and Local Music all feel like part of the same device.

---

# 9. OLED Now Playing screen

The OS should provide its own unified Now Playing screen.

It can be opened from the e-paper regardless of which application is currently playing.

OLED:

```text
NOW PLAYING                     ⋯


         ┌──────────────┐
         │              │
         │              │
         │  ALBUM ART   │
         │              │
         │              │
         └──────────────┘


          Night Drive
            Chromatics

         Kill for Love


02:14   ━━━━━━━━━━━━━━━   04:43
```

The OLED should prioritize:

1. artwork
2. track
3. artist
4. album
5. progress

Avoid putting large redundant play/pause controls on the OLED because they already exist on the e-paper.

---

# 10. E-paper Now Playing controls

When the unified Now Playing screen is open:

```text
┌──────────────────────────────┐
│ NIGHT DRIVE                  │
│ CHROMATICS                   │
│                              │
│      ◀◀      Ⅱ      ▶▶      │
│                              │
│ ♡       QUEUE        ⋯       │
└──────────────────────────────┘
```

The e-paper should therefore act almost like the control wheel/buttons of a traditional MP3 player.

Important touch targets should be large.

---

# 11. E-paper contextual behavior

The e-paper UI changes based on what the OLED is doing.

Examples:

OLED = Spotify browsing

E-paper:

```text
NIGHT DRIVE
Chromatics

   ◀◀       Ⅱ       ▶▶

HOME          NOW PLAYING
```

OLED = Settings

E-paper:

```text
SETTINGS

← BACK

HOME
```

OLED = Search

E-paper:

KEYBOARD

OLED = Local Music

E-paper:

```text
LOCAL MUSIC

HOME      SEARCH

NOW PLAYING
```

OLED = Queue

E-paper:

```text
QUEUE

◀◀       Ⅱ       ▶▶

SHUFFLE       CLEAR
```

The lower display therefore always provides actions relevant to the current OLED context.

---

# 12. Local Music application

Local Music should use the custom device UI rather than generic Android styling.

Main Local Music OLED page:

```text
LOCAL MUSIC


RECENTLY ADDED

[cover]   [cover]   [cover]


LIBRARY

Artists                     482
Albums                      127
Songs                      1842
Playlists                    14


SHUFFLE ALL
```

Artist page:

```text
ARTISTS

A

ABBA
Air
Arctic Monkeys


B

Beach House
Bon Iver


C

Chromatics
```

Album screen:

```text
            [ ALBUM ART ]

             Discovery

             Daft Punk
               2001


01  One More Time
02  Aerodynamic
03  Digital Love
04  Harder Better Faster Stronger
...
```

---

# 13. Queue

OLED:

```text
QUEUE


PLAYING

[art]  Night Drive
       Chromatics


NEXT

01  Back From the Grave
02  The Page
03  Lady
04  These Streets Will Never Look...
05  Broken Mirrors
```

E-paper:

```text
NIGHT DRIVE

   ◀◀       Ⅱ       ▶▶

SHUFFLE         CLEAR
```

---

# 14. Device sleep mode

Sleep mode is a major product feature.

When entering Sleep mode:

OLED:

OFF

Completely black.

No always-on OLED UI.

The e-paper becomes the primary display.

---

# 15. E-paper Sleep screen

The default Sleep screen should use album artwork.

Convert album art into a monochrome or grayscale dithered representation appropriate for e-paper.

Example:

```text
┌──────────────────────────────┐
│                              │
│       ┌──────────────┐       │
│       │              │       │
│       │ DITHERED     │       │
│       │ ALBUM COVER  │       │
│       │              │       │
│       └──────────────┘       │
│                              │
│        NIGHT DRIVE           │
│         Chromatics           │
│                              │
│          ▶ PLAYING           │
│                              │
│ 14:32                    72%  │
└──────────────────────────────┘
```

Paused:

```text
          Ⅱ PAUSED
```

Playing:

```text
          ▶ PLAYING
```

The Sleep screen should show:

- album artwork
- song
- artist
- playback state
- time
- battery

Optional:

- Bluetooth state
- output device

---

# 16. Sleep mode refresh strategy

E-paper should not continuously update.

Refresh when:

- song changes
- playback changes between play / pause
- output device changes
- battery crosses meaningful thresholds
- user interacts
- significant system state changes

Do NOT refresh the display every second for track progress.

Do NOT render an animated progress bar.

A track change should trigger a clean full or partial refresh depending on panel capabilities.

Album artwork should be preprocessed asynchronously.

---

# 17. Alternative Sleep screen: Audiophile mode

Allow the user to select an information-focused Sleep display.

Example:

```text
NOW PLAYING


NIGHT DRIVE
Chromatics

Kill for Love

────────────────────────────

▶ PLAYING

44.1 kHz / 24 bit

USB DAC
WIRED OUTPUT

14:32                    72%
```

Possible modes:

- Artwork
- Minimal
- Audiophile
- Clock

---

# 18. OLED ambient clock mode

There is an important distinction between:

IDLE

and

SLEEP.

IDLE:

- device awake
- OLED active
- e-paper launcher visible

SLEEP:

- OLED off
- e-paper Sleep screen visible

In IDLE mode the OLED should primarily behave as a clock.

Example:

```text
              14:32

              TUE
            25 AUG


          BATTERY 72%
```

This gives the device a very deliberate idle state instead of displaying an empty home screen.

---

# 19. Returning Home

There should always be an easy route back to the e-paper launcher.

For example, while Spotify is open:

```text
NIGHT DRIVE

 ◀◀       Ⅱ       ▶▶

HOME      NOW PLAYING
```

Touching HOME:

1. closes/minimizes the foreground application
2. returns OLED to ambient clock
3. changes e-paper to app launcher

This creates a consistent mental model:

HOME means:

OLED = clock / ambient

E-paper = application selection

---

# 20. Search and keyboard

Search is one of the most interesting uses of the e-paper.

When the user selects a search field on the OLED:

OLED:

```text
SEARCH


┌──────────────────────────────┐
│ Daft Punk_                   │
└──────────────────────────────┘


RECENT

Daft Punk
Justice
Air
```

The e-paper becomes the keyboard.

---

# 21. E-paper keyboard

Example:

```text
 Q  W  E  R  T  Y  U  I  O  P

  A  S  D  F  G  H  J  K  L

   Z  X  C  V  B  N  M   ⌫


123       SPACE       ENTER
```

Important:

DO NOT visually animate every key press on the e-paper.

Instead:

- touch key
- trigger short haptic pulse
- immediately update OLED text field
- keep e-paper keyboard mostly static

This avoids unnecessary ghosting and refreshes.

---

# 22. Symbols keyboard

```text
 1  2  3  4  5  6  7  8  9  0

 @  #  €  &  *  (  )  -  +

 .  ,  ?  !  '  "  :  ;  ⌫


ABC       SPACE       ENTER
```

---

# 23. Haptic feedback

The e-paper should feel almost like physical buttons.

Use the vibration motor to give subtle feedback for:

- keyboard presses
- play
- pause
- next
- previous
- app selection
- important switches

Haptic patterns should be short and precise.

Avoid smartphone-like long vibrations.

---

# 24. Settings

Settings should be a native custom interface.

Do not expose standard Android Settings as the primary settings interface.

OLED:

```text
SETTINGS


AUDIO
Output
Equalizer
Volume limit
Audio quality


DISPLAY
OLED
E-paper
Sleep display


CONNECTIVITY
Bluetooth
Wi-Fi


MUSIC
Streaming services
Local library
Downloads


DEVICE
Storage
Battery
System
About
```

---

# 25. E-paper settings behavior

While settings are open:

```text
SETTINGS


← BACK


HOME
```

For a settings page requiring selections, context-specific shortcuts may appear.

Example:

OLED:

```text
E-PAPER DISPLAY

Sleep Layout
Artwork                         >

Refresh Mode
Balanced                        >

Sleep after
30 seconds                      >

Show clock                     ON
Show battery                   ON
Show artwork                   ON
```

E-paper:

```text
E-PAPER

← BACK

PREVIEW

RESTORE DEFAULT
```

---

# 26. Quick settings

A downward swipe on the OLED should open Quick Settings.

OLED:

```text
14:32                         72%


BLUETOOTH
Sony WH-1000XM6


[ WI-FI ] [ OFFLINE ] [ LOCK ]


VOLUME

━━━━━━━━━━━━━━━━━━━━━━


BRIGHTNESS

━━━━━━━━━━━━━━━━━━━━━━


OUTPUT

Sony WH-1000XM6              >
```

E-paper simultaneously:

```text
QUICK CONTROL


VOL −       64%       VOL +


OUTPUT >
```

---

# 27. Application launcher behavior

The launcher should be modular.

Create an application data model similar to:

```kotlin
data class MusicAppEntry(
    val id: String,
    val label: String,
    val packageName: String?,
    val iconType: IconType,
    val action: LauncherAction
)
```

Example entries:

- Spotify
- Local Music
- Apple Music
- Settings

The launcher must allow additional applications later.

Avoid hard-coding the complete interface directly around the initial four applications.

---

# 28. Suggested e-paper launcher interactions

Preferred approach:

Tap application:

Open immediately.

Optional alternative:

First tap highlights.

Second tap opens.

However, because this is a touch device with large targets, direct single-tap launching is probably preferable.

Possible vertical layout:

```text
MUSIC

● Spotify

  Local Music

  Apple Music

────────────────

  Settings
```

Keep all controls reachable with a thumb.

---

# 29. OLED visual design system

OLED background:

near-black, not necessarily absolute black everywhere.

Example:

#0B0B0A

Primary text:

warm white.

Example:

#F1EFE9

Secondary text:

muted warm gray.

Avoid excessive accent colors.

Album artwork should usually be the strongest source of color.

Typography:

Use one high-quality sans-serif family.

Recommended character:

- clean
- slightly geometric
- not futuristic
- excellent numeric glyphs

Possible inspiration:

- Inter
- Geist
- IBM Plex Sans
- Neue Haas Grotesk style

Do not imitate these fonts if licensing prevents it.

---

# 30. OLED typography hierarchy

Suggested hierarchy:

Clock:
64–90sp

Screen title:
14–18sp
uppercase optional

Track title:
24–30sp

Artist:
16–20sp

Body:
14–18sp

Metadata:
11–14sp

The clock should feel like an intentional visual object.

---

# 31. E-paper visual design system

The e-paper should be monochrome.

Prefer:

- black
- white
- optional grayscale if panel supports it

Use:

- bold labels
- clean line icons
- large touch areas
- thin separators
- simple geometric symbols

Do not use:

- shadows
- gradients
- tiny text
- continuously changing graphics
- complex animations

---

# 32. E-paper typography

The e-paper can use the same family as the OLED but with slightly heavier weights.

Recommended:

Section labels:
11–13sp, uppercase

Menu labels:
16–22sp

Track:
16–20sp

Artist:
12–16sp

Buttons:
14–18sp

Because e-paper has lower visual fluidity, clarity is more important than density.

---

# 33. Icon philosophy

Use extremely simple icons.

Examples:

Play:
▶

Pause:
Ⅱ

Next:
▶▶

Previous:
◀◀

Favorite:
♡ / ♥

Home:
simple outline house OR text HOME

Queue:
three horizontal lines

Search:
magnifying glass

Back:
←

Prefer text labels when an icon might be ambiguous.

---

# 34. Motion

OLED animations should be subtle.

Examples:

- 150–250 ms fade
- slight vertical movement
- album art crossfade
- smooth list transitions

Avoid:

- bouncing
- spring-heavy Material animations
- dramatic page transitions

E-paper:

No conventional animation.

Transitions should be designed around deliberate state changes.

---

# 35. System architecture

Create a central UI state manager.

Conceptually:

```text
DeviceUiState

HOME_IDLE
APP_ACTIVE
NOW_PLAYING
SEARCH
SETTINGS
QUICK_SETTINGS
SLEEP
```

Also maintain:

```text
ActiveContext

activeApp
activeMediaSession
currentTrack
playbackState
outputDevice
battery
isSleeping
keyboardVisible
```

The e-paper renderer subscribes to changes in these states.

---

# 36. E-paper renderer architecture

Do not let random screens write directly to the e-paper.

Create one centralized component:

```text
EPaperUiController
```

Responsibilities:

- receive device state
- determine appropriate e-paper layout
- calculate whether a refresh is necessary
- choose partial/full refresh
- render text/icons/images
- process e-paper touch input
- send events back to the main UI/controller

Conceptually:

```text
Android media/app state
        ↓
DeviceUiState
        ↓
EPaperUiController
        ↓
EPaperRenderer
        ↓
Display hardware
```

---

# 37. E-paper screen types

Implement reusable screen components for:

```text
EPaperLauncherScreen
EPaperPlaybackScreen
EPaperSleepScreen
EPaperKeyboardScreen
EPaperNavigationScreen
EPaperQuickControlScreen
EPaperSettingsScreen
```

Do not build every state as an unrelated custom view.

Reuse shared components.

---

# 38. Album artwork processing

For sleep mode, create an artwork processing pipeline.

Input:

Color Bitmap

Processing:

1. crop to square
2. resize to e-paper artwork dimensions
3. convert to grayscale
4. increase contrast if needed
5. apply dithering
6. cache result

Potential algorithms:

- Floyd–Steinberg dithering
- ordered dithering
- grayscale quantization

Cache processed artwork by track/album identifier so it is not recalculated unnecessarily.

---

# 39. Physical controls

If the hardware includes physical volume buttons:

Volume buttons should always control media volume.

If there is a three-position slider, suggested mapping:

TOP:

LOCK

MIDDLE:

NORMAL

BOTTOM:

SLEEP

NORMAL:
regular UI

SLEEP:
OLED immediately switches off and e-paper Sleep interface appears

LOCK:
touch interactions are locked while playback continues

The exact mapping should be configurable later.

---

# 40. User journey example

Device wakes.

OLED:

```text
14:32

TUE 25 AUG
```

E-paper:

```text
SPOTIFY
LOCAL MUSIC
APPLE MUSIC
SETTINGS
```

User taps Spotify.

OLED:

Spotify opens.

E-paper:

```text
NIGHT DRIVE
Chromatics

◀◀     Ⅱ     ▶▶

HOME   NOW PLAYING
```

User selects a song in Spotify.

MediaSession metadata changes.

E-paper automatically updates track information.

User taps NOW PLAYING.

OLED switches to unified album-art Now Playing screen.

E-paper remains playback controls.

User stops interacting.

After configured idle timeout:

OLED turns off.

E-paper changes to:

```text
[DITHERED ALBUM ART]

NIGHT DRIVE
Chromatics

▶ PLAYING

14:32              72%
```

User touches the device.

OLED wakes into Now Playing.

User taps HOME.

OLED becomes clock.

E-paper becomes app launcher.

This interaction loop should define the entire device.

---

# 41. Most important UX requirement

The interface must never feel like:

"Android running on a weird two-screen device."

It should feel like:

"a music player whose operating system happens to be based on Android."

Android applications are implementation details.

The user should perceive one coherent music-oriented operating environment.

---

# 42. Priority order for implementation

Implement in this order:

1. Basic dual-display state architecture
2. E-paper app launcher
3. OLED idle clock
4. App launching from e-paper
5. Android MediaSession integration
6. Persistent e-paper playback controls
7. Unified OLED Now Playing
8. OLED-off Sleep mode
9. E-paper Sleep screen
10. Album-art dithering/cache
11. Local Music UI
12. Settings
13. E-paper keyboard
14. Quick Settings
15. physical slider integration
16. animations/polish

Focus first on interaction architecture rather than visual polish.

---

# 43. Final product identity

Three UI moments should visually define the device.

## Moment 1 — Idle

OLED:

large elegant clock.

E-paper:

music application launcher.

## Moment 2 — Listening

OLED:

beautiful full-color album artwork and metadata.

E-paper:

persistent physical-like playback controls.

## Moment 3 — Sleep

OLED:

completely off.

E-paper:

dithered album artwork, track, artist, playback state, clock and battery.

Those three states should be treated as the signature UI of the product.

The transition between them should feel deliberate, predictable and extremely simple.

The device should require very little explanation to operate.