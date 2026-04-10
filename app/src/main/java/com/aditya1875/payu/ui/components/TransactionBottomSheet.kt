package com.aditya1875.payu.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aditya1875.payu.domain.models.Transaction
import java.time.LocalDate

private data class CategoryMeta(
    val label: String,
    val icon: ImageVector,
    val gradientStart: Color,
    val gradientEnd: Color,
)

private val expenseCategories = listOf(
    CategoryMeta("Groceries", Icons.Outlined.ShoppingCart, Color(0xFF1B4332), Color(0xFF2D6A4F)),
    CategoryMeta("Bills", Icons.Outlined.Receipt, Color(0xFF1A1A2E), Color(0xFF16213E)),
    CategoryMeta("Shopping", Icons.Outlined.LocalMall, Color(0xFF4A0E8F), Color(0xFF7B2FBE)),
    CategoryMeta("Transport", Icons.Outlined.DirectionsCar, Color(0xFF1B2A4A), Color(0xFF2E4A8A)),
    CategoryMeta("Dining", Icons.Outlined.Restaurant, Color(0xFF4A1A00), Color(0xFF8B3A00)),
    CategoryMeta("Health", Icons.Outlined.LocalHospital, Color(0xFF1A3A1A), Color(0xFF2D5A2D)),
    CategoryMeta("Other", Icons.Outlined.Category, Color(0xFF2A2A2A), Color(0xFF3D3D3D)),
)

private val incomeCategories = listOf(
    CategoryMeta("Salary", Icons.Outlined.AccountBalance, Color(0xFF003322), Color(0xFF00563B)),
    CategoryMeta("Bonus", Icons.Outlined.CardGiftcard, Color(0xFF2B1700), Color(0xFF5C3300)),
    CategoryMeta("Freelance", Icons.Outlined.Laptop, Color(0xFF001A33), Color(0xFF003066)),
    CategoryMeta("Other", Icons.Outlined.Category, Color(0xFF2A2A2A), Color(0xFF3D3D3D)),
)

private val transferCategories = listOf(
    CategoryMeta(
        "Bank Transfer",
        Icons.Outlined.AccountBalance,
        Color(0xFF1A1A2E),
        Color(0xFF16213E)
    ),
    CategoryMeta("Wallet Transfer", Icons.Outlined.Wallet, Color(0xFF1B2A4A), Color(0xFF2E4A8A)),
    CategoryMeta("Other", Icons.Outlined.SwapHoriz, Color(0xFF2A2A2A), Color(0xFF3D3D3D)),
)

// ─── Bottom Sheet ─────────────────────────────────────────────────────────────

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    initialType: String = "Expense",
    onSaveClick: (Transaction) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedType by rememberSaveable { mutableStateOf(initialType) }
    val types = listOf("Income", "Expense", "Transfer")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            // Header
            Text(
                "New Transaction",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Type selector pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                types.forEach { type ->
                    val isSelected = selectedType == type
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.96f,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "typeScale"
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .scale(scale)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.onBackground
                                else Color.Transparent
                            )
                            .clickable { selectedType = type }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            type,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.background
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Form — animated crossfade between types
            AnimatedContent(
                targetState = selectedType,
                transitionSpec = {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(150))
                },
                label = "formTransition"
            ) { type ->
                TransactionForm(type = type, onSaveClick = onSaveClick)
            }
        }
    }
}

// ─── Form ─────────────────────────────────────────────────────────────────────

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionForm(
    type: String,
    onSaveClick: (Transaction) -> Unit
) {
    var amount by rememberSaveable { mutableStateOf("") }
    var selectedCat by rememberSaveable { mutableStateOf<CategoryMeta?>(null) }
    var customCategory by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf(LocalDate.now().toString()) }
    var notes by rememberSaveable { mutableStateOf("") }

    val categories = when (type) {
        "Income" -> incomeCategories
        "Expense" -> expenseCategories
        else -> transferCategories
    }

    val isExpense = type == "Expense"
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
    )

    Column {
        // Amount
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (₹)") },
            leadingIcon = {
                Text(
                    "₹",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // Category grid — with per-category gradient accent cards (expense only)
        Text(
            "Category",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3-column grid of category chips
        val chunked = categories.chunked(3)
        chunked.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { cat ->
                    val isSelected = selectedCat?.label == cat.label
                    val cardScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.04f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "catScale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .scale(cardScale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isExpense && isSelected)
                                    Brush.linearGradient(listOf(cat.gradientStart, cat.gradientEnd))
                                else if (isExpense)
                                    Brush.linearGradient(
                                        listOf(
                                            cat.gradientStart.copy(alpha = 0.35f),
                                            cat.gradientEnd.copy(alpha = 0.35f)
                                        )
                                    )
                                else
                                    Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                            )
                            .then(
                                if (isSelected)
                                    Modifier.border(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        RoundedCornerShape(12.dp)
                                    )
                                else Modifier
                            )
                            .clickable {
                                selectedCat = cat
                                customCategory = ""
                            }
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                cat.icon, contentDescription = cat.label,
                                tint = if (isExpense && isSelected) Color.White
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                cat.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isExpense && isSelected) Color.White
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
                // Pad incomplete rows
                repeat(3 - row.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // Subcategory / custom description text field — appears when a category is chosen
        AnimatedVisibility(
            visible = selectedCat != null,
            enter = fadeIn(tween(200)) + expandVertically(),
            exit = fadeOut(tween(150)) + shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = customCategory,
                    onValueChange = { customCategory = it },
                    label = { Text("What did you spend on? (optional)") },
                    placeholder = {
                        Text(
                            "e.g. Zomato dinner, Metro card…",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors,
                    singleLine = true,
                    leadingIcon = {
                        selectedCat?.let { cat ->
                            Icon(
                                cat.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        // Date
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            leadingIcon = {
                Icon(
                    Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        // Notes
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Notes,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            minLines = 2,
            maxLines = 3
        )

        Spacer(Modifier.height(20.dp))

        // Save button
        val buttonLabel = "Save $type"
        val isValid = amount.toDoubleOrNull() != null && amount.isNotBlank() && selectedCat != null

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull() ?: 0.0
                val finalCategory = if (customCategory.isNotBlank())
                    "${selectedCat?.label} — $customCategory"
                else
                    selectedCat?.label ?: ""

                onSaveClick(
                    Transaction(
                        id = 0,
                        amount = amt,
                        category = finalCategory,
                        type = type.lowercase(),
                        note = notes,
                        timestamp = System.currentTimeMillis()
                    )
                )
            },
            enabled = isValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                buttonLabel,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun getCategoryIcon(category: String): ImageVector {
    return when {
        category.contains("food", true) -> Icons.Default.Restaurant
        category.contains("travel", true) -> Icons.Default.Flight
        category.contains("shopping", true) -> Icons.Default.ShoppingCart
        category.contains("bills", true) -> Icons.Default.Receipt
        category.contains("salary", true) -> Icons.Default.AttachMoney
        category.contains("transport", true) -> Icons.Default.DirectionsCar
        else -> Icons.Default.Category
    }
}