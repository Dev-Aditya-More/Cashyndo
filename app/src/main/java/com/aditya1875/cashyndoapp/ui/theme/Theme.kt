// Theme.kt
package com.aditya1875.cashyndoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary          = BrandTealLight,      // cyan-teal buttons / active states
    onPrimary        = DarkBackground,
    primaryContainer = BrandTeal,
    onPrimaryContainer = OnDark,

    secondary        = BrandAmber,          // amber highlights
    onSecondary      = DarkBackground,
    secondaryContainer = Color(0xFF3A2800),
    onSecondaryContainer = BrandAmberLight,

    tertiary         = IncomeGreen,
    onTertiary       = DarkBackground,

    error            = ExpenseRed,
    onError          = DarkBackground,

    background       = DarkBackground,
    onBackground     = OnDark,

    surface          = DarkSurface,
    onSurface        = OnDark,

    surfaceVariant   = DarkSurfaceVar,
    onSurfaceVariant = OnDarkMuted,

    outline          = OnDarkMuted,
)

// ── Light scheme ─────────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = BrandTeal,
    onPrimary        = LightSurface,
    primaryContainer = Color(0xFFB2DFDF),
    onPrimaryContainer = Color(0xFF003737),

    secondary        = BrandAmber,
    onSecondary      = LightSurface,
    secondaryContainer = Color(0xFFFFECB3),
    onSecondaryContainer = Color(0xFF4E3100),

    tertiary         = IncomeGreen,
    onTertiary       = LightSurface,

    error            = ExpenseRed,
    onError          = LightSurface,

    background       = LightBackground,
    onBackground     = OnLight,

    surface          = LightSurface,
    onSurface        = OnLight,

    surfaceVariant   = Color(0xFFE8EDF2),
    onSurfaceVariant = OnLightMuted,

    outline          = OnLightMuted,
)

private val CashyndoTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize   = 57.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 36.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 28.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 22.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 18.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 16.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight  = 24.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight  = 20.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 14.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 12.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize   = 11.sp,
        letterSpacing = 0.5.sp
    )
)

private val CashyndoShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun CashyndoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color intentionally disabled so brand colors are always applied
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = CashyndoTypography,
        shapes      = CashyndoShapes,
        content     = content
    )
}