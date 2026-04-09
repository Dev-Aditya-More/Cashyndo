package com.aditya1875.payu.ui.presentation.home.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aditya1875.payu.domain.models.Transaction
import com.aditya1875.payu.ui.components.AddTransactionBottomSheet
import com.aditya1875.payu.ui.presentation.home.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController? = null) {

    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val fullName = FirebaseAuth.getInstance().currentUser?.displayName
    val firstName = fullName?.split(" ")?.firstOrNull() ?: "there"
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("Weekly") }

    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val surface = MaterialTheme.colorScheme.surface
    val primary = MaterialTheme.colorScheme.primary

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top Bar ──────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
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
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                Box {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    // Badge
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.Red)
                            .align(Alignment.TopEnd)
                            .offset(x = (-4).dp, y = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "2",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            // ── Greeting ─────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "Hey, $firstName",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Add your yesterday's expense",
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceVariant
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Bank Card ────────────────────────────
            BankCard(
                cardNumber = "8763 1111 2222 0329",
                cardHolder = firstName.uppercase(),
                expiryDate = "10/28",
                bankName = "ADRBank",
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Your expenses",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Weekly / Monthly Toggle ──────────────
            PeriodToggle(
                selected = selectedPeriod,
                onSelect = { selectedPeriod = it },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ── Expense Category Cards ───────────────
            if (state.recentTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions yet", color = onSurfaceVariant)
                }
            } else {
                // Group by category and show expense cards
                val expenses = state.recentTransactions
                    .filter { it.type == "expense" }
                    .groupBy { it.category }

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    expenses.forEach { (category, txns) ->
                        val total = txns.sumOf { it.amount }
                        val prevTotal = total * 0.9 // mock comparison

                        ExpenseCategoryCard(
                            category = category,
                            amount = "₹$total",
                            trend = if (total > prevTotal) "More than last week" else "Lesser than last week"
                        )
                    }

                    if (expenses.isEmpty()) {
                        // Show sample cards if no expenses
                        ExpenseCategoryCard(
                            "FOOD",
                            "₹${state.totalExpense}",
                            "Lesser than last week"
                        )
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
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
                onDismiss = { showBottomSheet = false }
            )
        }
    }
}

@Composable
fun BankCard(
    cardNumber: String,
    cardHolder: String,
    expiryDate: String,
    bankName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE8D5B7),
                        Color(0xFF8EC5B2),
                        Color(0xFF4ABFAF)
                    )
                )
            )
            .padding(22.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    bankName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                // Card logo circle
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    // Two overlapping circles
                    Row {
                        Box(
                            Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        Box(
                            Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White.copy(alpha = 0.5f))
                                .offset(x = (-8).dp)
                        )
                    }
                }
            }

            // Card number
            Text(
                cardNumber,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            )

            // Bottom row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Card Holder Name",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )
                    Text(
                        cardHolder,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Expired Date",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )
                    Text(
                        expiryDate,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PeriodToggle(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Weekly", "Monthly").forEach { period ->
                val isSelected = selected == period
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) onBackground else Color.Transparent)
                        .clickable { onSelect(period) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        period,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) background else onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseCategoryCard(
    category: String,
    amount: String,
    trend: String
) {
    var starred by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                // Two overlapping circles as per Figma
                Row {
                    Box(
                        Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    )
                    Box(
                        Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                            .offset(x = (-6).dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    category.uppercase(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    trend,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Star
            IconButton(onClick = { starred = !starred }) {
                Icon(
                    imageVector = if (starred) Icons.Default.Star else Icons.Outlined.StarBorder,
                    contentDescription = null,
                    tint = if (starred) Color(0xFFFFCC00) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Amount pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    amount,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}