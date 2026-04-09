package com.aditya1875.payu.ui.theme

import androidx.compose.ui.graphics.Brush

val CardGradient = Brush.linearGradient(
    colors = listOf(
        AccentPrimary.copy(alpha = 0.4f),
        AccentSecondary.copy(alpha = 0.4f)
    )
)

val GlowGradient = Brush.radialGradient(
    colors = listOf(
        AccentSecondary.copy(alpha = 0.3f),
        AccentTertiary.copy(alpha = 0.1f)
    )
)