package com.aditya1875.payu.ui.presentation.home.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aditya1875.payu.domain.models.Transaction
import com.aditya1875.payu.ui.components.AddTransactionBottomSheet
import com.aditya1875.payu.ui.components.getCategoryIcon
import com.aditya1875.payu.ui.presentation.home.components.TiltBankCard
import com.aditya1875.payu.ui.presentation.home.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {

    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val fullName = FirebaseAuth.getInstance().currentUser?.displayName
    val firstName = fullName?.split(" ")?.firstOrNull() ?: "there"

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("Weekly") }

    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val infiniteTransition = rememberInfiniteTransition(label = "fabPulse")
    val fabGlow by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "fabGlow"
    )
    val fabScale by animateFloatAsState(
        targetValue = if (showBottomSheet) 0.88f else fabGlow,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "fabScale"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = background,
        floatingActionButton = {
            Box(
                Modifier
                    .scale(fabScale)
                    .shadow(20.dp, CircleShape, spotColor = onBackground.copy(alpha = 0.35f))
            ) {
                FloatingActionButton(
                    onClick = { showBottomSheet = true },
                    containerColor = onBackground,
                    contentColor = background,
                    shape = CircleShape
                ) { Icon(Icons.Default.Add, contentDescription = "Add") }
            }
        }
    ) { innerPadding ->

        val context = LocalContext.current

        Column(
            modifier = Modifier
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
                IconButton(onClick = {
                    Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                IconButton(onClick = {
                    Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }

            // ── Greeting ─────────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "Hey, $firstName",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "Add your yesterday's expense",
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceVariant
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Tilt Bank Card ───────────────────────────────────────────
            TiltBankCard(
                cardNumber = "8763 1111 2222 0329",
                cardHolder = firstName.uppercase(),
                expiryDate = "10/28",
                bankName = "ADRBank",
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Your expenses",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(12.dp))

            PeriodToggle(
                selected = selectedPeriod, onSelect = { selectedPeriod = it },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ── Expense cards only ────────────────────────────────────────
            val expenses = state.recentTransactions
                .filter { it.type == "expense" }
                .groupBy { it.category }

            if (expenses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) { Text("No expenses yet", color = onSurfaceVariant) }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    expenses.entries.forEachIndexed { index, (category, txns) ->
                        val total = txns.sumOf { it.amount }
                        val prev = total * 0.9
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(index * 80L)
                            visible = true
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 2 }) {
                            ExpenseCategoryCard(
                                category = category,
                                amount = "₹${"%.0f".format(total)}",
                                trend = if (total > prev) "More than last week" else "Lesser than last week"
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }

        if (showBottomSheet) {
            AddTransactionBottomSheet(
                initialType = "Expense",
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
private fun PeriodToggle(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = MaterialTheme.colorScheme.background
    val onBg = MaterialTheme.colorScheme.onBackground
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(Modifier.fillMaxWidth()) {
            listOf("Weekly", "Monthly").forEach { period ->
                val isSelected = selected == period
                val scale by animateFloatAsState(
                    if (isSelected) 1f else 0.95f,
                    spring(stiffness = Spring.StiffnessMediumLow), label = "ts"
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .scale(scale)
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) onBg else Color.Transparent)
                        .clickable { onSelect(period) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        period, style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) bg else muted
                        )
                    )
                }
            }
        }
    }
}

private val categoryAccents: Map<String, Color> = mapOf(
    "Groceries" to Color(0xFF2D6A4F),
    "Bills" to Color(0xFF3A86FF),
    "Shopping" to Color(0xFF9B5DE5),
    "Transport" to Color(0xFF4361EE),
    "Dining" to Color(0xFFE76F51),
    "Health" to Color(0xFF52B788),
    "Food" to Color(0xFFE76F51),
    "Travel" to Color(0xFF4361EE),
    "Other" to Color(0xFF8D99AE),
)

@Composable
private fun ExpenseCategoryCard(category: String, amount: String, trend: String) {
    var starred by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardScale by animateFloatAsState(
        if (isPressed) 0.97f else 1f,
        spring(stiffness = Spring.StiffnessMedium), label = "cs"
    )
    val starScale by animateFloatAsState(
        if (starred) 1.3f else 1f,
        spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "ss"
    )

    val accentKey =
        categoryAccents.keys.firstOrNull { category.contains(it, ignoreCase = true) } ?: "Other"
    val accentColor = categoryAccents[accentKey] ?: Color(0xFF8D99AE)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clickable(interactionSource = interactionSource, indication = null) {},
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(accentColor.copy(alpha = 0.9f), accentColor.copy(alpha = 0.2f))
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        category.uppercase(),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        trend,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { starred = !starred }, modifier = Modifier.scale(starScale)) {
                    Icon(
                        if (starred) Icons.Default.Star else Icons.Outlined.StarBorder, null,
                        tint = if (starred) Color(0xFFFFCC00) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.13f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        amount, style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold, color = accentColor
                        )
                    )
                }
            }
        }
    }
}