package com.aditya1875.cashyndoapp.ui.presentation.home.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aditya1875.cashyndoapp.domain.models.Transaction
import com.aditya1875.cashyndoapp.ui.components.AddTransactionBottomSheet
import com.aditya1875.cashyndoapp.ui.navigation.Route
import com.aditya1875.cashyndoapp.ui.presentation.home.components.BalanceCard
import com.aditya1875.cashyndoapp.ui.presentation.home.components.MiniStatChip
import com.aditya1875.cashyndoapp.ui.presentation.home.components.SummaryCard
import com.aditya1875.cashyndoapp.ui.presentation.home.components.TransactionRow
import com.aditya1875.cashyndoapp.ui.presentation.home.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController? = null) {

    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val fullName = FirebaseAuth.getInstance().currentUser?.displayName
    val firstName = fullName?.split(" ")?.firstOrNull() ?: "there"

    var showBottomSheet by remember { mutableStateOf(false) }

    val primary = MaterialTheme.colorScheme.primary
    val error = MaterialTheme.colorScheme.error
    val tertiary = MaterialTheme.colorScheme.tertiary
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val total = state.totalIncome + state.totalExpense

    val incomeFraction =
        if (total == 0.0) 0f else (state.totalIncome / total).toFloat()

    val expenseFraction =
        if (total == 0.0) 0f else (state.totalExpense / total).toFloat()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Hey, $firstName",
                style = MaterialTheme.typography.titleLarge,
                color = onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            BalanceCard(state.totalBalance)

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard(
                        label = "INCOME",
                        amount = "₹${state.totalIncome}",
                        barFraction = incomeFraction,
                        barColor = primary,
                        icon = Icons.Outlined.ArrowUpward,
                        iconBg = primary.copy(alpha = 0.1f),
                        iconTint = primary
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    SummaryCard(
                        label = "EXPENSE",
                        amount = "₹${state.totalExpense}",
                        barFraction = expenseFraction,
                        barColor = error,
                        icon = Icons.Outlined.ArrowDownward,
                        iconBg = error.copy(alpha = 0.1f),
                        iconTint = error
                    )
                }
            }

            Spacer(Modifier.height(35.dp))

            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium
                )

                if (state.recentTransactions.isNotEmpty()) {
                    TextButton(
                        onClick = { navController?.navigate(Route.Transactions.route) }
                    ) {
                        Text("View All")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            if (state.recentTransactions.isEmpty()) {
                Text("No transactions yet", color = onSurfaceVariant)
            } else {
                state.recentTransactions.forEach { txn ->
                    TransactionRow(txn)
                    HorizontalDivider()
                }
            }
        }

        // Bottom Sheet
        if (showBottomSheet) {
            AddTransactionBottomSheet(
                onSaveClick = { txnUI ->
                    viewModel.addTransaction(
                        Transaction(
                            id = 0,
                            amount = txnUI.amount,
                            category = txnUI.category,
                            type = txnUI.type.lowercase(),
                            note = txnUI.note,
                            timestamp = System.currentTimeMillis()
                        )
                    )

                    showBottomSheet = false
                },
                onDismiss = {
                    showBottomSheet = false
                }
            )
        }
    }
}

//@Composable
//private fun DonutChart(
//    segments: List<SpendingCategory>,
//    modifier: Modifier = Modifier
//) {
//    val background = MaterialTheme.colorScheme.background
//    Canvas(modifier = modifier) {
//        val strokeWidth = size.minDimension * 0.14f
//        val radius = (min(size.width, size.height) - strokeWidth) / 2f
//        val topLeft = Offset((size.width - radius * 2) / 2f, (size.height - radius * 2) / 2f)
//        val arcSize = Size(radius * 2, radius * 2)
//
//        var startAngle = -90f
//        segments.forEach { seg ->
//            val sweep = 360f * seg.pct / 100f
//            drawArc(
//                color = seg.color,
//                startAngle = startAngle,
//                sweepAngle = sweep - 2f,
//                useCenter = false,
//                topLeft = topLeft,
//                size = arcSize,
//                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
//            )
//            startAngle += sweep
//        }
//    }
//}