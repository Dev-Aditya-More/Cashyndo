package com.aditya1875.payu.ui.presentation.transaction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aditya1875.payu.ui.presentation.transaction.viewmodel.TransactionViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.cos
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionsScreen(navController: NavController) {

    val viewModel: TransactionViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val surface = MaterialTheme.colorScheme.surface
    val primary = MaterialTheme.colorScheme.primary
    val error = MaterialTheme.colorScheme.error

    val creditScore = 660
    val currencies = listOf(
        Triple("CAD", "Canadian Dollar", "\uD83C\uDDE8\uD83C\uDDE6"),
        Triple("USD", "US Dollar", "\uD83C\uDDFA\uD83C\uDDF8"),
        Triple("EUR", "Euro", "\uD83C\uDDEA\uD83C\uDDFA")
    )

    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul")
    val barData = listOf(400f, 800f, 300f, 600f, 500f, 700f, 250f)
    val maxBar = barData.max()

    val transaction = state.transactions.sumOf {
        it.amount.coerceAtLeast(350.0)
    }

    val aprilBudget = transaction * 1.83f

    Scaffold(
        containerColor = background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = onBackground,
                contentColor = background,
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "P",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = background
                        )
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    "PayU",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                Box {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Red)
                            .align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("2", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp))
                    }
                }
            }

            // ── Title ────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "Your Balances",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "Manage your multi-currency accounts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceVariant
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Credit Score Gauge ───────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                CreditScoreGauge(score = creditScore)
            }

            Spacer(Modifier.height(32.dp))

            // ── Available Currencies ─────────────────
            Text(
                "Available Currencies",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(12.dp))

            currencies.forEach { (code, name, flag) ->
                var starred by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(flag, fontSize = 24.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                code,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                name,
                                style = MaterialTheme.typography.bodySmall,
                                color = onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { starred = !starred }) {
                            Icon(
                                if (starred) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = null,
                                tint = if (starred) Color(0xFFFFCC00) else onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "+ Enable",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(16.dp))

            // ── Spending Bar Chart ───────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                SpendingBarChart(
                    data = barData,
                    labels = months,
                    maxValue = maxBar
                )
            }

            Spacer(Modifier.height(12.dp))

            // Current margin
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Current margin: April Spendings",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "$${"%.2f".format(transaction)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text("/", style = MaterialTheme.typography.bodySmall, color = onSurfaceVariant)
                    Text(
                        "$${"%.2f".format(aprilBudget)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

@Composable
private fun CreditScoreGauge(score: Int) {
    val teal = Color(0xFF4ABFAF)
    val pink = Color(0xFFE879A5)
    val blue = Color(0xFF5B9BD5)
    val yellow = Color(0xFFF5C842)

    Box(
        modifier = Modifier
            .size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2
            val cy = size.height / 2
            val radius = size.minDimension / 2 - 20.dp.toPx()
            val strokeW = 20.dp.toPx()
            val startAngle = 180f
            val totalSweep = 180f

            // Dotted background arc
            val dotCount = 40
            for (i in 0..dotCount) {
                val angle = startAngle + (totalSweep / dotCount) * i
                val rad = Math.toRadians(angle.toDouble())
                val x = (cx + radius * cos(rad)).toFloat()
                val y = (cy + radius * sin(rad)).toFloat()
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = 3.dp.toPx(),
                    center = Offset(x, y)
                )
            }

            // Teal segment (large, left side)
            drawArc(
                color = teal,
                startAngle = 180f,
                sweepAngle = 80f,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Pink segment
            drawArc(
                color = pink,
                startAngle = 265f,
                sweepAngle = 45f,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Blue segment
            drawArc(
                color = blue,
                startAngle = 313f,
                sweepAngle = 25f,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Yellow segment (small, right end)
            drawArc(
                color = yellow,
                startAngle = 341f,
                sweepAngle = 18f,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Indicator dot at ~313° (blue/yellow junction)
            val indicatorAngle = Math.toRadians(338.0)
            val indicatorX = (cx + radius * cos(indicatorAngle)).toFloat()
            val indicatorY = (cy + radius * sin(indicatorAngle)).toFloat()
            drawCircle(color = Color.White, radius = 10.dp.toPx(), center = Offset(indicatorX, indicatorY))
            drawCircle(color = blue, radius = 6.dp.toPx(), center = Offset(indicatorX, indicatorY))
        }

        // Center content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = 16.dp)
        ) {
            Text(
                score.toString(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                )
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

@Composable
private fun SpendingBarChart(
    data: List<Float>,
    labels: List<String>,
    maxValue: Float
) {
    val teal = Color(0xFF4ABFAF)
    val surface = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val gridLines = listOf(0f, 200f, 500f, 1000f)
    val chartHeight = 160.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(chartHeight + 32.dp)
    ) {
        // Y-axis labels + grid lines
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
                        "\$${"%.0f".format(v)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = onSurfaceVariant,
                        modifier = Modifier.width(40.dp)
                    )
                    HorizontalDivider(
                        color = onSurfaceVariant.copy(alpha = 0.15f),
                        thickness = 1.dp,
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
                .padding(start = 48.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { i, value ->
                val fraction = value / maxValue
                val isHighlighted = i == 1 // Feb as tallest bar

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
                                        colors = listOf(
                                            surface,
                                            surface.copy(alpha = 0.3f)
                                        )
                                    )
                                else
                                    Brush.verticalGradient(
                                        colors = listOf(
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
}