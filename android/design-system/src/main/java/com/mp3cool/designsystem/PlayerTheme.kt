package com.mp3cool.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object PlayerColors {
    val Ink = Color(0xFF10100F)
    val Paper = Color(0xFFF1EBDD)
    val Signal = Color(0xFFE7683C)
    val Muted = Color(0xFF9E998E)
}

object PlayerTypography {
    val Eyebrow = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
    val Display = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 34.sp, lineHeight = 38.sp, fontWeight = FontWeight.Black)
    val Body = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 16.sp, lineHeight = 22.sp)
}

@Composable
fun Mp3CoolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = PlayerColors.Signal,
            background = PlayerColors.Ink,
            surface = PlayerColors.Ink,
            onBackground = PlayerColors.Paper,
            onSurface = PlayerColors.Paper,
        ),
        content = content,
    )
}
