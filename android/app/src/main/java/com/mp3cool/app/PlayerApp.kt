package com.mp3cool.app

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp3cool.designsystem.PlayerColors
import com.mp3cool.designsystem.PlayerTypography
import com.mp3cool.hardware.api.AudioRouteManager
import com.mp3cool.hardware.api.BatteryManager
import com.mp3cool.hardware.api.EpaperContent
import com.mp3cool.hardware.api.EpaperManager
import com.mp3cool.hardware.api.HapticManager
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private enum class OledPage { LOCAL_MUSIC, ARTISTS, QUEUE, SEARCH, SETTINGS, QUICK_SETTINGS }

@Composable
fun PlayerApp(
    epaperManager: EpaperManager,
    batteryManager: BatteryManager,
    audioRouteManager: AudioRouteManager,
    hapticManager: HapticManager,
) {
    val epaperController = remember(epaperManager) { EPaperUiController(epaperManager) }
    val epaperState by epaperManager.state.collectAsState()
    val batteryState by batteryManager.batteryState.collectAsState()
    val activeOutput by audioRouteManager.activeOutput.collectAsState()
    val now = rememberMinuteTicker()

    var model by remember { mutableStateOf(DeviceUiModel()) }
    var oledPage by remember { mutableStateOf<OledPage?>(null) }

    val context = model.context.copy(
        batteryPercentage = batteryState.percentage,
        outputDevice = activeOutput.displayName(),
    )
    val syncedModel = model.copy(context = context)

    LaunchedEffect(syncedModel) {
        epaperController.command(syncedModel)
    }

    fun update(next: DeviceUiModel, page: OledPage? = oledPage) {
        model = next
        oledPage = page
    }

    fun goHome() {
        hapticManager.selectionTick()
        update(
            syncedModel.copy(mode = DeviceMode.HOME_IDLE, selectedLauncherIndex = 0, context = syncedModel.context.copy(activeApp = null)),
            page = null,
        )
    }

    fun openLauncherEntry(index: Int) {
        val entry = syncedModel.launcherEntries[index]
        hapticManager.lightClick()
        val nextContext = syncedModel.context.copy(activeApp = entry)
        when (entry.action) {
            LauncherAction.OPEN_LOCAL_MUSIC -> update(
                syncedModel.copy(mode = DeviceMode.APP_ACTIVE, selectedLauncherIndex = index, context = nextContext),
                page = OledPage.LOCAL_MUSIC,
            )
            LauncherAction.OPEN_SETTINGS -> update(
                syncedModel.copy(mode = DeviceMode.SETTINGS, selectedLauncherIndex = index, context = nextContext),
                page = OledPage.SETTINGS,
            )
            LauncherAction.OPEN_EXTERNAL_APP -> update(
                syncedModel.copy(mode = DeviceMode.APP_ACTIVE, selectedLauncherIndex = index, context = nextContext),
                page = null,
            )
        }
    }

    fun openNowPlaying() {
        hapticManager.selectionTick()
        update(syncedModel.copy(mode = DeviceMode.NOW_PLAYING), page = null)
    }

    fun openSearch() {
        hapticManager.selectionTick()
        update(syncedModel.copy(mode = DeviceMode.SEARCH), page = OledPage.SEARCH)
    }

    fun togglePlay() {
        hapticManager.lightClick()
        update(syncedModel.copy(context = syncedModel.context.copy(isPlaying = !syncedModel.context.isPlaying)))
    }

    fun sleepDevice() {
        hapticManager.selectionTick()
        update(syncedModel.copy(mode = DeviceMode.SLEEP), page = null)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(PlayerColors.Ink)
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Crossfade(targetState = syncedModel.mode to oledPage, animationSpec = tween(180), label = "oled") { (mode, page) ->
                when (mode) {
                    DeviceMode.HOME_IDLE -> AmbientHomeScreen(now, syncedModel, onQuickSettings = {
                        update(syncedModel.copy(mode = DeviceMode.QUICK_SETTINGS), page = OledPage.QUICK_SETTINGS)
                    })
                    DeviceMode.APP_ACTIVE -> when (page) {
                        OledPage.LOCAL_MUSIC -> LocalMusicScreen(
                            onArtists = { update(syncedModel, page = OledPage.ARTISTS) },
                            onSearch = ::openSearch,
                            onNowPlaying = ::openNowPlaying,
                        )
                        OledPage.ARTISTS -> ArtistsScreen(onBack = { update(syncedModel, page = OledPage.LOCAL_MUSIC) })
                        else -> ExternalAppScreen(syncedModel.context.activeApp, onNowPlaying = ::openNowPlaying)
                    }
                    DeviceMode.NOW_PLAYING -> NowPlayingScreen(
                        syncedModel,
                        onQueue = { update(syncedModel, page = OledPage.QUEUE) },
                        onSleep = ::sleepDevice,
                    )
                    DeviceMode.SEARCH -> SearchScreen(
                        query = syncedModel.context.keyboardQuery,
                        onQueryChange = { query ->
                            update(syncedModel.copy(context = syncedModel.context.copy(keyboardQuery = query)), page = OledPage.SEARCH)
                        },
                    )
                    DeviceMode.SETTINGS -> SettingsScreen(
                        onSleep = ::sleepDevice,
                        onQuickSettings = { update(syncedModel.copy(mode = DeviceMode.QUICK_SETTINGS), page = OledPage.QUICK_SETTINGS) },
                    )
                    DeviceMode.QUICK_SETTINGS -> QuickSettingsScreen(
                        model = syncedModel,
                        onVolume = { volume -> update(syncedModel.copy(context = syncedModel.context.copy(volume = volume)), page = OledPage.QUICK_SETTINGS) },
                    )
                    DeviceMode.SLEEP -> SleepOledScreen()
                }
            }
        }

        EpaperDevelopmentSurface(
            content = epaperState.content,
            awake = epaperState.awake,
            onLauncherTap = ::openLauncherEntry,
            onHome = ::goHome,
            onNowPlaying = ::openNowPlaying,
            onTogglePlay = ::togglePlay,
            onQueue = { update(syncedModel.copy(mode = DeviceMode.NOW_PLAYING), page = OledPage.QUEUE) },
            onSearch = ::openSearch,
            onBack = {
                hapticManager.selectionTick()
                update(syncedModel.copy(mode = DeviceMode.APP_ACTIVE), page = OledPage.LOCAL_MUSIC)
            },
            onKeyboard = { token ->
                hapticManager.lightClick()
                val current = syncedModel.context.keyboardQuery
                val next = when (token) {
                    "SPACE" -> "$current "
                    "BACKSPACE" -> current.dropLast(1)
                    "ENTER" -> current
                    else -> current + token.lowercase()
                }
                update(syncedModel.copy(context = syncedModel.context.copy(keyboardQuery = next)), page = OledPage.SEARCH)
            },
        )
    }
}

@Composable
private fun AmbientHomeScreen(now: LocalTime, model: DeviceUiModel, onQuickSettings: () -> Unit) {
    val date = LocalDate.now()
    val day = date.dayOfWeek.name.take(3)
    val month = date.month.name.take(3)
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 34.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("MP3COOL", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
            Text("${model.context.batteryPercentage}%", style = PlayerTypography.Eyebrow, color = PlayerColors.Paper, modifier = Modifier.clickable { onQuickSettings() })
        }
        Spacer(Modifier.weight(1f))
        Text(now.format(DateTimeFormatter.ofPattern("HH:mm")), fontSize = 78.sp, lineHeight = 84.sp, fontWeight = FontWeight.Light, color = PlayerColors.Paper)
        Text("$day  ${date.dayOfMonth} $month", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        Spacer(Modifier.weight(1f))
        if (model.context.isPlaying) {
            MiniNowPlaying(model)
        } else {
            Text("CONNECTED / ${model.context.outputDevice.uppercase()}", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        }
    }
}

@Composable
private fun MiniNowPlaying(model: DeviceUiModel) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        AlbumArt(model.context.currentTrack, Modifier.size(74.dp), compact = true)
        Spacer(Modifier.width(18.dp))
        Column(Modifier.width(190.dp)) {
            Text(model.context.currentTrack.title.uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PlayerColors.Paper, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(model.context.currentTrack.artist, fontSize = 14.sp, color = PlayerColors.Muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ExternalAppScreen(entry: MusicAppEntry?, onNowPlaying: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(34.dp)) {
        TopLine(title = entry?.label?.uppercase() ?: "APPLICATION", action = "NOW PLAYING", onAction = onNowPlaying)
        Spacer(Modifier.weight(1f))
        Text(entry?.label?.uppercase() ?: "APP", style = PlayerTypography.Display, color = PlayerColors.Paper)
        Spacer(Modifier.height(12.dp))
        Text("Android application surface", style = PlayerTypography.Body, color = PlayerColors.Muted)
        Spacer(Modifier.height(26.dp))
        Text("The e-paper panel remains the persistent playback and home control layer.", style = PlayerTypography.Body, color = PlayerColors.Muted)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun LocalMusicScreen(onArtists: () -> Unit, onSearch: () -> Unit, onNowPlaying: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(34.dp)) {
        TopLine("LOCAL MUSIC", "SEARCH", onSearch)
        Spacer(Modifier.height(34.dp))
        Text("RECENTLY ADDED", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            DemoMusic.recentlyAdded.forEach { track ->
                Column(Modifier.width(104.dp)) {
                    AlbumArt(track, Modifier.size(104.dp), compact = true)
                    Spacer(Modifier.height(8.dp))
                    Text(track.title, fontSize = 13.sp, color = PlayerColors.Paper, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(track.artist, fontSize = 12.sp, color = PlayerColors.Muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
        Spacer(Modifier.height(34.dp))
        Text("LIBRARY", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        Spacer(Modifier.height(12.dp))
        LibraryRow("Artists", "482", onArtists)
        LibraryRow("Albums", "127") { }
        LibraryRow("Songs", "1842") { }
        LibraryRow("Playlists", "14") { }
        Spacer(Modifier.height(28.dp))
        Text("SHUFFLE ALL", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PlayerColors.Signal, modifier = Modifier.clickable { onNowPlaying() })
    }
}

@Composable
private fun ArtistsScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(34.dp)) {
        TopLine("ARTISTS", "BACK", onBack)
        Spacer(Modifier.height(26.dp))
        DemoMusic.artists.forEach { (letter, artists) ->
            Text(letter, style = PlayerTypography.Eyebrow, color = PlayerColors.Signal)
            Spacer(Modifier.height(10.dp))
            artists.forEach { artist ->
                Text(artist, fontSize = 22.sp, color = PlayerColors.Paper, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
            }
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun NowPlayingScreen(model: DeviceUiModel, onQueue: () -> Unit, onSleep: () -> Unit) {
    val track = model.context.currentTrack
    Column(Modifier.fillMaxSize().padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TopLine("NOW PLAYING", "SLEEP", onSleep)
        Spacer(Modifier.weight(0.55f))
        AlbumArt(track, Modifier.size(240.dp), compact = false)
        Spacer(Modifier.height(28.dp))
        Text(track.title, fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold, color = PlayerColors.Paper, textAlign = TextAlign.Center)
        Text(track.artist, fontSize = 18.sp, color = PlayerColors.Muted)
        Text(track.album, fontSize = 15.sp, color = PlayerColors.Muted)
        Spacer(Modifier.height(28.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(track.positionLabel, style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
            Box(Modifier.weight(1f).padding(horizontal = 14.dp).height(2.dp).background(Color(0xFF3A3833))) {
                Box(Modifier.fillMaxWidth(0.48f).height(2.dp).background(PlayerColors.Paper))
            }
            Text(track.durationLabel, style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        }
        Spacer(Modifier.height(24.dp))
        Text("QUEUE", style = PlayerTypography.Eyebrow, color = PlayerColors.Signal, modifier = Modifier.clickable { onQueue() })
        Spacer(Modifier.weight(0.45f))
    }
}

@Composable
private fun SearchScreen(query: String, onQueryChange: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(34.dp)) {
        TopLine("SEARCH", "CLEAR", { onQueryChange("") })
        Spacer(Modifier.height(42.dp))
        Box(Modifier.fillMaxWidth().height(68.dp).background(Color(0xFF181816), RoundedCornerShape(6.dp)).padding(horizontal = 18.dp), contentAlignment = Alignment.CenterStart) {
            Text(if (query.isEmpty()) "_" else "${query}_", fontSize = 28.sp, color = PlayerColors.Paper, maxLines = 1)
        }
        Spacer(Modifier.height(34.dp))
        Text("RECENT", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        listOf("Daft Punk", "Justice", "Air").forEach {
            Text(it, fontSize = 21.sp, color = PlayerColors.Paper, modifier = Modifier.padding(top = 18.dp))
        }
    }
}

@Composable
private fun QuickSettingsScreen(model: DeviceUiModel, onVolume: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().padding(34.dp)) {
        TopLine("QUICK CONTROL", "${model.context.batteryPercentage}%", {})
        Spacer(Modifier.height(32.dp))
        Text("BLUETOOTH", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        Text(model.context.outputDevice, fontSize = 23.sp, color = PlayerColors.Paper)
        Spacer(Modifier.height(30.dp))
        SettingChipRow(listOf("WI-FI", "OFFLINE", "LOCK"))
        Spacer(Modifier.height(32.dp))
        Text("VOLUME", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        Slider(value = model.context.volume.toFloat(), onValueChange = { onVolume(it.toInt()) }, valueRange = 0f..100f)
        Spacer(Modifier.height(20.dp))
        Text("BRIGHTNESS", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        Slider(value = 72f, onValueChange = {}, valueRange = 0f..100f)
    }
}

@Composable
private fun SettingsScreen(onSleep: () -> Unit, onQuickSettings: () -> Unit) {
    val groups = linkedMapOf(
        "AUDIO" to listOf("Output", "Equalizer", "Volume limit", "Audio quality"),
        "DISPLAY" to listOf("OLED", "E-paper", "Sleep display"),
        "CONNECTIVITY" to listOf("Bluetooth", "Wi-Fi"),
        "MUSIC" to listOf("Streaming services", "Local library", "Downloads"),
        "DEVICE" to listOf("Storage", "Battery", "System", "About"),
    )
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(34.dp)) {
        TopLine("SETTINGS", "QUICK", onQuickSettings)
        Spacer(Modifier.height(28.dp))
        groups.forEach { (title, entries) ->
            Text(title, style = PlayerTypography.Eyebrow, color = PlayerColors.Signal)
            entries.forEach { entry ->
                Row(Modifier.fillMaxWidth().padding(vertical = 9.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(entry, fontSize = 18.sp, color = PlayerColors.Paper)
                    Text(">", fontSize = 18.sp, color = PlayerColors.Muted)
                }
            }
            Spacer(Modifier.height(18.dp))
        }
        Text("SLEEP NOW", style = PlayerTypography.Eyebrow, color = PlayerColors.Signal, modifier = Modifier.clickable { onSleep() })
    }
}

@Composable
private fun SleepOledScreen() {
    Box(Modifier.fillMaxSize().background(Color.Black))
}

@Composable
private fun EpaperDevelopmentSurface(
    content: EpaperContent,
    awake: Boolean,
    onLauncherTap: (Int) -> Unit,
    onHome: () -> Unit,
    onNowPlaying: () -> Unit,
    onTogglePlay: () -> Unit,
    onQueue: () -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit,
    onKeyboard: (String) -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(238.dp)
            .background(Color(0xFFECE9DF))
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        when (content) {
            is EpaperContent.Launcher -> EpaperLauncher(content, onLauncherTap)
            is EpaperContent.Playback, is EpaperContent.Player -> EpaperPlayback(content, onHome, onNowPlaying, onTogglePlay, onQueue)
            is EpaperContent.Sleep -> EpaperSleep(content)
            is EpaperContent.Keyboard -> EpaperKeyboard(content.query, onKeyboard)
            is EpaperContent.QuickControl -> EpaperQuickControl(content, onBack)
            is EpaperContent.Settings -> EpaperNavigation(content.title, content.actions, onBack, onHome)
            is EpaperContent.Navigation -> EpaperNavigation(content.title, content.actions, onBack, onHome)
            is EpaperContent.Library -> EpaperNavigation(content.title, content.entries, onBack, onSearch)
            is EpaperContent.Volume -> EpaperQuickControl(EpaperContent.QuickControl(content.level, "Output"), onBack)
            EpaperContent.Empty -> Text(if (awake) "READY" else "ASLEEP", color = Color.Black)
        }
    }
}

@Composable
private fun EpaperLauncher(content: EpaperContent.Launcher, onLauncherTap: (Int) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text(content.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        EpaperRule()
        content.entries.forEachIndexed { index, entry ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { onLauncherTap(index) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(if (index == content.selectedIndex) "●" else " ", fontSize = 18.sp, color = Color.Black, modifier = Modifier.width(28.dp))
                Text(entry, fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
private fun EpaperPlayback(content: EpaperContent, onHome: () -> Unit, onNowPlaying: () -> Unit, onTogglePlay: () -> Unit, onQueue: () -> Unit) {
    val title: String
    val artist: String
    val playing: Boolean
    val primary: String
    val secondary: String
    when (content) {
        is EpaperContent.Playback -> {
            title = content.title
            artist = content.artist
            playing = content.isPlaying
            primary = content.primaryAction
            secondary = content.secondaryAction
        }
        is EpaperContent.Player -> {
            title = content.title
            artist = content.artist
            playing = content.isPlaying
            primary = "HOME"
            secondary = "NOW PLAYING"
        }
        else -> return
    }
    Column(Modifier.fillMaxSize()) {
        Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(artist, fontSize = 14.sp, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            EpaperButton("◀◀") {}
            EpaperButton(if (playing) "Ⅱ" else "▶", onTogglePlay)
            EpaperButton("▶▶") {}
        }
        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            EpaperButton(if (primary == "QUEUE") "♡" else primary, if (primary == "QUEUE") onQueue else onHome)
            EpaperButton(secondary, if (secondary == "NOW PLAYING") onNowPlaying else onQueue)
        }
    }
}

@Composable
private fun EpaperSleep(content: EpaperContent.Sleep) {
    Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        DitheredArtwork(Modifier.size(132.dp))
        Spacer(Modifier.width(20.dp))
        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(content.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(content.artist, fontSize = 14.sp, color = Color.Black)
            }
            Text(if (content.isPlaying) "▶ PLAYING" else "Ⅱ PAUSED", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(content.clock, fontSize = 13.sp, color = Color.Black)
                Text("${content.batteryPercentage}%", fontSize = 13.sp, color = Color.Black)
            }
        }
    }
}

@Composable
private fun EpaperKeyboard(query: String, onKeyboard: (String) -> Unit) {
    val rows = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        rows.forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { char -> EpaperButton(char.toString()) { onKeyboard(char.toString()) } }
                if (row == "ZXCVBNM") EpaperButton("⌫") { onKeyboard("BACKSPACE") }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            EpaperButton("123") {}
            EpaperButton("SPACE") { onKeyboard("SPACE") }
            EpaperButton("ENTER") { onKeyboard("ENTER") }
        }
        Text(query, fontSize = 11.sp, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun EpaperQuickControl(content: EpaperContent.QuickControl, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Text("QUICK CONTROL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            EpaperButton("VOL -") {}
            Text("${content.volume}%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            EpaperButton("VOL +") {}
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(content.output.uppercase(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
            EpaperButton("BACK", onBack)
        }
    }
}

@Composable
private fun EpaperNavigation(title: String, actions: List<String>, onBack: () -> Unit, onHome: () -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Text(title.uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Column {
            actions.forEach { action ->
                EpaperButton(
                    label = if (action == "BACK") "← BACK" else action.uppercase(),
                    onClick = if (action == "HOME") onHome else onBack,
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun AlbumArt(track: MockTrack, modifier: Modifier, compact: Boolean) {
    val base = Color(track.color)
    Box(
        modifier
            .clip(RoundedCornerShape(if (compact) 4.dp else 6.dp))
            .background(Brush.linearGradient(listOf(base, Color(0xFF11100F))))
            .padding(12.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(Color.White.copy(alpha = 0.18f), radius = size.minDimension * 0.34f, center = Offset(size.width * 0.72f, size.height * 0.28f))
            drawRect(Color.Black.copy(alpha = 0.26f), topLeft = Offset(size.width * 0.12f, size.height * 0.18f), size = Size(size.width * 0.5f, size.height * 0.5f), style = Stroke(width = 2.dp.toPx()))
        }
        if (!compact) {
            Text(track.album.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.86f))
        }
    }
}

@Composable
private fun DitheredArtwork(modifier: Modifier) {
    Canvas(modifier.background(Color.White)) {
        val step = size.width / 13f
        for (x in 0..12) {
            for (y in 0..12) {
                val dark = (x * 3 + y * 5) % 7 < 3
                drawRect(
                    color = if (dark) Color.Black else Color(0xFFBEBEBE),
                    topLeft = Offset(x * step, y * step),
                    size = Size(step * 0.72f, step * 0.72f),
                )
            }
        }
        drawRect(Color.Black, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
private fun TopLine(title: String, action: String, onAction: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
        Text(action, style = PlayerTypography.Eyebrow, color = PlayerColors.Signal, modifier = Modifier.clickable { onAction() })
    }
}

@Composable
private fun LibraryRow(label: String, count: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 22.sp, color = PlayerColors.Paper)
        Text(count, fontSize = 22.sp, color = PlayerColors.Muted)
    }
}

@Composable
private fun SettingChipRow(labels: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        labels.forEach { label ->
            Box(Modifier.background(Color(0xFF20201D), RoundedCornerShape(4.dp)).padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(label, style = PlayerTypography.Eyebrow, color = PlayerColors.Paper)
            }
        }
    }
}

@Composable
private fun EpaperButton(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        color = Color.Black,
        fontSize = 15.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable { onClick() },
    )
}

@Composable
private fun EpaperRule() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
}

@Composable
private fun rememberMinuteTicker(): LocalTime {
    var time by remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            time = LocalTime.now()
            delay(60_000)
        }
    }
    return time
}
