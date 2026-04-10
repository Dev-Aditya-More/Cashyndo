package com.aditya1875.payu.ui.presentation.balances.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aditya1875.payu.domain.models.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryTransactionCard(
    txn: Transaction,
    onDelete: () -> Unit
) {
    val isIncome = txn.type == "income"
    val accentColor = accentFor(txn.category)
    val incomeColor = MaterialTheme.colorScheme.tertiary
    val errorColor = MaterialTheme.colorScheme.error

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "cardScale"
    )

    // Swipe-to-delete hint: show delete button animated
    var deleteVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            // Left accent strip — teal for income, category colour for expense
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(
                        Brush.verticalGradient(
                            if (isIncome)
                                listOf(
                                    incomeColor.copy(alpha = 0.9f),
                                    incomeColor.copy(alpha = 0.2f)
                                )
                            else
                                listOf(
                                    accentColor.copy(alpha = 0.9f),
                                    accentColor.copy(alpha = 0.2f)
                                )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isIncome) incomeColor.copy(alpha = 0.12f)
                            else accentColor.copy(alpha = 0.12f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isIncome) Icons.Outlined.TrendingUp else Icons.Outlined.ShoppingBag,
                        contentDescription = null,
                        tint = if (isIncome) incomeColor else accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        txn.category,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    if (txn.note.isNotBlank()) {
                        Text(
                            txn.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                    // Timestamp
                    val dateStr = remember(txn.timestamp) {
                        SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                            .format(Date(txn.timestamp))
                    }
                    Text(
                        dateStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (isIncome) "+₹${"%.0f".format(txn.amount)}"
                        else "-₹${"%.0f".format(txn.amount)}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isIncome) incomeColor else errorColor
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    // Delete — small text button
                    TextButton(
                        onClick = onDelete,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            Icons.Outlined.DeleteOutline, contentDescription = "Delete",
                            tint = errorColor.copy(alpha = 0.7f), modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            "Delete", color = errorColor.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
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
    "Salary" to Color(0xFF00C07F),
    "Bonus" to Color(0xFFF5A623),
    "Freelance" to Color(0xFF4ABFAF),
    "Other" to Color(0xFF8D99AE),
)

private fun accentFor(category: String): Color {
    val key = categoryAccents.keys.firstOrNull { category.contains(it, ignoreCase = true) }
    return categoryAccents[key] ?: Color(0xFF8D99AE)
}