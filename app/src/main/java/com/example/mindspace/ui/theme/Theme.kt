package com.example.mindspace.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7DD3FC),      // Sky Blue
    secondary = Color(0xFFC4B5FD),    // Lavender Purple
    background = Color(0xFF0F172A),   // Very Dark Navy
    surface = Color(0xFF1E293B),      // Charcoal Blue
    onPrimary = Color(0xFF0F172A),    // Contrast for primary
    onSecondary = Color(0xFF0F172A),  // Contrast for secondary
    onBackground = Color(0xFFF1F5F9), // Light Gray
    onSurface = Color(0xFFF1F5F9),    // Light Gray
)

@Composable
fun MindSpaceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}