package com.example.mindspace.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Modern meditation-inspired color palette
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6366F1),           // Indigo
    primaryContainer = Color(0xFF4F46E5),  // Darker Indigo
    secondary = Color(0xFF8B5CF6),         // Purple
    secondaryContainer = Color(0xFF7C3AED), // Darker Purple
    tertiary = Color(0xFF06B6D4),          // Cyan
    tertiaryContainer = Color(0xFF0891B2), // Darker Cyan

    background = Color(0xFF0A0A0F),        // Deep Space Blue
    surface = Color(0xFF1A1B23),           // Dark Surface
    surfaceVariant = Color(0xFF2A2D3A),    // Surface Variant
    surfaceContainer = Color(0xFF1E1F26),  // Container Surface

    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFE2E8F0),      // Light Text
    onSurface = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF94A3B8),   // Muted Text

    outline = Color(0xFF475569),            // Border Color
    outlineVariant = Color(0xFF334155),     // Subtle Border

    error = Color(0xFFEF4444),
    errorContainer = Color(0xFF7F1D1D),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFFFECDD3),

    // Custom gradient colors for backgrounds
    inverseSurface = Color(0xFF6366F1),     // For gradient start
    inverseOnSurface = Color(0xFF8B5CF6),   // For gradient end
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6366F1),
    primaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF8B5CF6),
    secondaryContainer = Color(0xFFF3E8FF),
    tertiary = Color(0xFF06B6D4),
    tertiaryContainer = Color(0xFFCFFAFE),

    background = Color(0xFFFAFAFA),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF1F5F9),
    surfaceContainer = Color(0xFFF8FAFC),

    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF64748B),

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),

    error = Color(0xFFDC2626),
    errorContainer = Color(0xFFFEE2E2),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF991B1B),
)

@Composable
fun MindSpaceTheme(
    darkTheme: Boolean = true, // Always dark for meditation app
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

// Custom colors for special effects
object MindSpaceColors {
    val gradientStart = Color(0xFF6366F1)
    val gradientMiddle = Color(0xFF8B5CF6)
    val gradientEnd = Color(0xFF06B6D4)

    val shimmerHighlight = Color(0xFF3B4EF0)
    val glassBackground = Color(0x1AFFFFFF)
    val glassBorder = Color(0x33FFFFFF)

    val successGreen = Color(0xFF10B981)
    val warningAmber = Color(0xFFF59E0B)

    val breathingBlue = Color(0xFF7DD3FC)
    val meditationPurple = Color(0xFFC4B5FD)
}