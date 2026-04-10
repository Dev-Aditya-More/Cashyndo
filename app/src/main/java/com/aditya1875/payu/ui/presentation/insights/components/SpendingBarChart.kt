package com.aditya1875.payu.ui.presentation.insights.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SpendingBarChart(
    data: List<Float>,
    labels: List<String>,
    maxValue: Float
) {
    val teal = Color(0xFF4ABFAF)
    val surfaceVar = MaterialTheme.colorScheme.surfaceVariant
    val onSurfVar = MaterialTheme.colorScheme.onSurfaceVariant
    val chartHeight = 160.dp
    val highlightIdx = data.indices.maxByOrNull { data[it] } ?: 1

    // Animate bar heights
    val animatedFractions = data.mapIndexed { _, value ->
        val target = (value / maxValue).coerceIn(0f, 1f)
        val anim by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(700, easing = FastOutSlowInEasing),
            label = "barAnim"
        )
        anim
    }

    val gridLines = listOf(0f, 200f, 500f, 1000f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight + 32.dp)
        ) {
            // Grid lines
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                gridLines.reversed().forEach { v ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "₹${"%.0f".format(v)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = onSurfVar,
                            modifier = Modifier.width(44.dp)
                        )
                        HorizontalDivider(
                            color = onSurfVar.copy(alpha = 0.15f), thickness = 1.dp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
                    .padding(start = 52.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                animatedFractions.forEachIndexed { i, fraction ->
                    val isHighlighted = i == highlightIdx
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .fillMaxHeight(fraction)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (isHighlighted)
                                        Brush.verticalGradient(
                                            listOf(
                                                surfaceVar,
                                                surfaceVar.copy(alpha = 0.3f)
                                            )
                                        )
                                    else
                                        Brush.verticalGradient(
                                            listOf(
                                                teal.copy(alpha = 0.9f),
                                                teal.copy(alpha = 0.3f)
                                            )
                                        )
                                )
                        )
                    }
                }
            }
        }

        // Month labels under bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 52.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            labels.forEach { label ->
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfVar,
                    modifier = Modifier.width(20.dp),
                    maxLines = 1
                )
            }
        }
    }
}