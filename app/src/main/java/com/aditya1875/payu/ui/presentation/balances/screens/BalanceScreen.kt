package com.aditya1875.payu.ui.presentation.balances.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aditya1875.payu.ui.presentation.balances.components.BalancePill
import com.aditya1875.payu.ui.presentation.balances.components.HistoryTransactionCard
import com.aditya1875.payu.ui.presentation.balances.viewmodel.TransactionViewModel
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalancesScreen() {

    val viewModel: TransactionViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle()
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle()
    val balance by viewModel.balance.collectAsStateWithLifecycle()

    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val onSurfVar = MaterialTheme.colorScheme.onSurfaceVariant
    val background = MaterialTheme.colorScheme.background
    val onBackground = MaterialTheme.colorScheme.onBackground

    val chips = listOf("ALL", "INCOME", "EXPENSE")

    Scaffold(
        containerColor = background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BalancePill(
                        "Balance",
                        "₹${"%.0f".format(balance)}",
                        primary,
                        Modifier.weight(1f)
                    )
                    BalancePill(
                        "Income",
                        "₹${"%.0f".format(totalIncome)}",
                        MaterialTheme.colorScheme.tertiary,
                        Modifier.weight(1f)
                    )
                    BalancePill(
                        "Expense",
                        "₹${"%.0f".format(totalExpense)}",
                        MaterialTheme.colorScheme.error,
                        Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.query,
                    onValueChange = { viewModel.onSearchChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = { Text("Search transactions...", color = onSurfVar) },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    trailingIcon = {
                        if (state.query.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchChange("") }) {
                                Icon(
                                    Icons.Outlined.Close, contentDescription = "Clear",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        focusedContainerColor = surface,
                        unfocusedContainerColor = surface,
                    )
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chips.forEach { chip ->
                        val isSelected = state.selectedFilter == chip
                        val chipScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.04f else 1f,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            label = "chipScale"
                        )
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onFilterChange(chip) },
                            label = { Text(chip, style = MaterialTheme.typography.labelMedium) },
                            modifier = Modifier.scale(chipScale),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = surface,
                                labelColor = onSurfVar
                            )
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    ) { innerPadding ->

        if (state.filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = null,
                        tint = onSurfVar, modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No transactions found",
                        style = MaterialTheme.typography.bodyLarge, color = onSurfVar
                    )
                    if (state.query.isNotEmpty() || state.selectedFilter != "ALL") {
                        Spacer(Modifier.height(6.dp))
                        TextButton(onClick = {
                            viewModel.onSearchChange("")
                            viewModel.onFilterChange("ALL")
                        }) {
                            Text("Clear filters", color = primary)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(
                    items = state.filteredTransactions,
                    key = { _, txn -> txn.id }
                ) { index, txn ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(txn.id) {
                        kotlinx.coroutines.delay(index.coerceAtMost(10) * 40L)
                        visible = true
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(250)) + slideInVertically(tween(250)) { it / 3 }
                    ) {
                        Column {
                            Spacer(Modifier.height(8.dp))
                            HistoryTransactionCard(
                                txn = txn,
                                onDelete = { viewModel.deleteTransaction(txn) }
                            )
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}