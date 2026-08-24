package com.mp3cool.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.mp3cool.designsystem.PlayerColors
import com.mp3cool.designsystem.PlayerTypography

private enum class Destination(val route: String, val label: String, val marker: String) {
    Home("home", "HOME", "01"), Library("library", "LIBRARY", "02"), Spotify("spotify", "SPOTIFY", "03"),
    Search("search", "SEARCH", "04"), Settings("settings", "SETTINGS", "05")
}

private enum class DetailDestination(val route: String, val title: String) {
    NowPlaying("now-playing", "NOW PLAYING"), Albums("albums", "ALBUMS"), Artists("artists", "ARTISTS"),
    Songs("songs", "SONGS"), Playlists("playlists", "PLAYLISTS"), Favorites("favorites", "FAVORITES"),
    RecentlyAdded("recently-added", "RECENTLY ADDED"), Downloads("downloads", "DOWNLOADS")
}

@Composable
fun PlayerApp() {
    val navController = rememberNavController()
    val entry = navController.currentBackStackEntryAsState().value
    val current = entry?.destination
    Row(Modifier.fillMaxSize().background(PlayerColors.Ink).systemBarsPadding()) {
        Column(
            Modifier.width(92.dp).fillMaxHeight().background(Color(0xFF181816)).padding(vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("MP3\nCOOL", style = PlayerTypography.Eyebrow, color = PlayerColors.Signal, modifier = Modifier.padding(horizontal = 16.dp))
            Column {
                Destination.entries.forEach { destination ->
                    val selected = current?.hierarchy?.any { it.route == destination.route } == true
                    Column(
                        Modifier.fillMaxWidth().clickable {
                            navController.navigate(destination.route) { popUpTo(Destination.Home.route); launchSingleTop = true }
                        }.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(destination.marker, fontSize = 10.sp, color = if (selected) PlayerColors.Signal else PlayerColors.Muted)
                        Text(destination.label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (selected) PlayerColors.Paper else PlayerColors.Muted)
                    }
                }
            }
            Text("0.1", style = PlayerTypography.Eyebrow, color = PlayerColors.Muted, modifier = Modifier.padding(horizontal = 16.dp))
        }
        NavHost(navController, startDestination = Destination.Home.route, modifier = Modifier.weight(1f)) {
            composable(Destination.Home.route) {
                PlaceholderScreen(
                    "HOME", "Your music, first.",
                    linkedMapOf(
                        "NOW PLAYING" to DetailDestination.NowPlaying.route,
                        "RECENTLY ADDED" to DetailDestination.RecentlyAdded.route,
                        "LOCAL MUSIC" to Destination.Library.route,
                        "DOWNLOADS" to DetailDestination.Downloads.route,
                    ), { route -> navController.navigate(route) },
                )
            }
            composable(Destination.Library.route) {
                PlaceholderScreen(
                    "LIBRARY", "Everything you carry.",
                    linkedMapOf(
                        "ALBUMS" to DetailDestination.Albums.route,
                        "ARTISTS" to DetailDestination.Artists.route,
                        "SONGS" to DetailDestination.Songs.route,
                        "PLAYLISTS" to DetailDestination.Playlists.route,
                        "FAVORITES" to DetailDestination.Favorites.route,
                        "FOLDERS" to null,
                    ), { route -> navController.navigate(route) },
                )
            }
            composable(Destination.Spotify.route) { PlaceholderScreen("SPOTIFY", "Authorized integration placeholder.", listOf("CONNECT", "RECENT", "OFFLINE — OFFICIAL APP")) }
            composable(Destination.Search.route) { PlaceholderScreen("SEARCH", "Local unified search arrives in Milestone 4.", listOf("TRACKS", "ARTISTS", "ALBUMS", "PLAYLISTS")) }
            composable(Destination.Settings.route) { PlaceholderScreen("SETTINGS", "Device controls without the phone feel.", listOf("WI-FI", "BLUETOOTH", "AUDIO", "E-PAPER", "HAPTICS", "ABOUT")) }
            DetailDestination.entries.forEach { detail ->
                composable(detail.route) {
                    PlaceholderScreen(detail.title, "Feature foundation ready for its scheduled milestone.", emptyList())
                }
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String, subtitle: String, sections: List<String>) {
    PlaceholderScreen(title, subtitle, sections.associateWith { null }) { }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    subtitle: String,
    sections: Map<String, String?>,
    navigate: (String) -> Unit,
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp)) {
        Text("MUSIC PLAYER / $title", style = PlayerTypography.Eyebrow, color = PlayerColors.Signal)
        Spacer(Modifier.height(18.dp))
        Text(title, style = PlayerTypography.Display, color = PlayerColors.Paper)
        Text(subtitle, style = PlayerTypography.Body, color = PlayerColors.Muted)
        Spacer(Modifier.height(36.dp))
        sections.entries.forEachIndexed { index, (section, route) ->
            Row(
                Modifier.fillMaxWidth().then(if (route != null) Modifier.clickable { navigate(route) } else Modifier).padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("${index + 1}".padStart(2, '0'), style = PlayerTypography.Eyebrow, color = PlayerColors.Muted)
                Spacer(Modifier.width(20.dp))
                Text(section, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = PlayerColors.Paper)
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF34332F)))
        }
    }
}
