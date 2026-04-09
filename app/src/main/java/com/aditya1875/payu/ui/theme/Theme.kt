// Theme.kt
package com.aditya1875.payu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = DarkBackground,

    secondary = AccentSecondary,
    onSecondary = DarkBackground,

    tertiary = AccentTertiary,

    background = DarkBackground,
    onBackground = OnDark,

    surface = DarkSurface,
    onSurface = OnDark,

    surfaceVariant = DarkSurfaceSoft,
    onSurfaceVariant = OnDarkMuted,

    error = ExpenseRed,
    onError = DarkBackground,

    outline = OnDarkMuted
)
private val CashyndoTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = OnDarkMuted
    )
)

private val CashyndoShapes = Shapes(
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun CashyndoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color intentionally disabled so brand colors are always applied
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CashyndoTypography,
        shapes = CashyndoShapes,
        content = content
    )
}