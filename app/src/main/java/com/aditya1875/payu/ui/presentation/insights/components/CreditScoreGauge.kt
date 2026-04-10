package com.aditya1875.payu.ui.presentation.insights.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CreditScoreGauge(score: Int) {
    val teal = Color(0xFF4ABFAF)
    val pink = Color(0xFFE879A5)
    val blue = Color(0xFF5B9BD5)
    val yellow = Color(0xFFF5C842)

    // Animate score arc on composition
    val animatedProgress by animateFloatAsState(
        targetValue = score / 850f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "scoreAnim"
    )

    Box(modifier = Modifier.size(240.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2
            val cy = size.height / 2
            val radius = size.minDimension / 2 - 20.dp.toPx()
            val strokeW = 20.dp.toPx()

            // Dotted background arc
            for (i in 0..40) {
                val angle = 180.0 + (180.0 / 40) * i
                val rad = Math.toRadians(angle)
                val x = (cx + radius * cos(rad)).toFloat()
                val y = (cy + radius * sin(rad)).toFloat()
                drawCircle(
                    color = Color.White.copy(alpha = 0.12f),
                    radius = 3.dp.toPx(),
                    center = Offset(x, y)
                )
            }

            // Teal segment
            drawArc(
                color = teal,
                startAngle = 180f,
                sweepAngle = 80f * animatedProgress * (850f / score.coerceAtLeast(1)),
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )
            // Pink segment
            drawArc(
                color = pink,
                startAngle = 265f,
                sweepAngle = 45f * animatedProgress,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )
            // Blue segment
            drawArc(
                color = blue,
                startAngle = 313f,
                sweepAngle = 25f * animatedProgress,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )
            // Yellow segment
            drawArc(
                color = yellow,
                startAngle = 341f,
                sweepAngle = 18f * animatedProgress,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Indicator dot
            val indicatorAngle = Math.toRadians(338.0)
            val indicatorX = (cx + radius * cos(indicatorAngle)).toFloat()
            val indicatorY = (cy + radius * sin(indicatorAngle)).toFloat()
            drawCircle(
                color = Color.White,
                radius = 10.dp.toPx(),
                center = Offset(indicatorX, indicatorY)
            )
            drawCircle(color = blue, radius = 6.dp.toPx(), center = Offset(indicatorX, indicatorY))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = 16.dp)
        ) {
            Text(
                score.toString(),
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Your Credit Score is average",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                "Last Check on 21 Apr",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}