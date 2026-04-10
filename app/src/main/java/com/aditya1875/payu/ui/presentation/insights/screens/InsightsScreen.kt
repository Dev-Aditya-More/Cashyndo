package com.aditya1875.payu.ui.presentation.insights.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aditya1875.payu.ui.presentation.balances.viewmodel.TransactionViewModel
import com.aditya1875.payu.ui.presentation.insights.components.CreditScoreGauge
import com.aditya1875.payu.ui.presentation.insights.components.SpendingBarChart
import com.aditya1875.payu.ui.presentation.insights.components.SummaryPill
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsightsScreen() {
    val viewModel: TransactionViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle()
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle()
    val balance by viewModel.balance.collectAsStateWithLifecycle()

    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val surface = MaterialTheme.colorScheme.surface
    val primary = MaterialTheme.colorScheme.primary

    val monthLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul")
    val barData: List<Float> = remember(state.transactions) {
        if (state.transactions.isEmpty()) {
            listOf(400f, 800f, 300f, 600f, 500f, 700f, 250f)
        } else {
            val expenses = state.transactions.filter { it.type == "expense" }
            if (expenses.isEmpty()) return@remember listOf(400f, 800f, 300f, 600f, 500f, 700f, 250f)
            val sorted = expenses.sortedBy { it.timestamp }
            val min = sorted.first().timestamp
            val max = sorted.last().timestamp.coerceAtLeast(min + 1)
            val bucket = (max - min) / 7.0
            val buckets = Array(7) { 0f }
            sorted.forEach { t ->
                val idx = ((t.timestamp - min) / bucket).toInt().coerceIn(0, 6)
                buckets[idx] += t.amount.toFloat()
            }
            buckets.toList()
        }
    }
    val maxBar = barData.maxOrNull()?.coerceAtLeast(1f) ?: 1f

    // ── Spend budget shown under chart ────────────────────────────────────────
    val budgetMultiplier = 1.83f
    val spentDisplay = totalExpense
    val budgetDisplay = totalExpense * budgetMultiplier

    // ── Currency rows ─────────────────────────────────────────────────────────
    val currencies = listOf(
        Triple("CAD", "Canadian Dollar", "\uD83C\uDDE8\uD83C\uDDE6"),
        Triple("USD", "US Dollar", "\uD83C\uDDFA\uD83C\uDDF8"),
        Triple("EUR", "Euro", "\uD83C\uDDEA\uD83C\uDDFA")
    )

    Scaffold(
        containerColor = background,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top bar ───────────────────────────────────────────────────────
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
                        "P", style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold, color = background
                        )
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    "PayU",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            // ── Title ─────────────────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "Your Balances",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "Manage your multi-currency accounts",
                    style = MaterialTheme.typography.bodyMedium, color = onSurfaceVariant
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Summary pills ─────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryPill(
                    label = "Balance",
                    amount = "₹${"%.0f".format(balance)}",
                    color = primary,
                    modifier = Modifier.weight(1f)
                )
                SummaryPill(
                    label = "Income",
                    amount = "₹${"%.0f".format(totalIncome)}",
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
                SummaryPill(
                    label = "Expense",
                    amount = "₹${"%.0f".format(totalExpense)}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Credit Score Gauge ────────────────────────────────────────────
            // Credit score is a static UI element (not derived from transactions)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                CreditScoreGauge(score = 660)
            }

            Spacer(Modifier.height(32.dp))

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

            // ── Spending Bar Chart ────────────────────────────────────────────
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
                SpendingBarChart(
                    data = barData,
                    labels = monthLabels,
                    maxValue = maxBar
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Spending margin row ───────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Current margin: Total Spendings",
                    style = MaterialTheme.typography.bodySmall, color = onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "₹${"%.0f".format(spentDisplay)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text("/", style = MaterialTheme.typography.bodySmall, color = onSurfaceVariant)
                    Text(
                        "₹${"%.0f".format(budgetDisplay)}",
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