package com.aditya1875.cashyndoapp.ui.presentation.transaction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aditya1875.cashyndoapp.domain.models.Transaction
import com.aditya1875.cashyndoapp.ui.presentation.transaction.viewmodel.TransactionViewModel
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(navController: NavController) {

    val viewModel: TransactionViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val onSurfVar = MaterialTheme.colorScheme.onSurfaceVariant

    val chips = listOf("ALL", "INCOME", "EXPENSE")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                // Search
                OutlinedTextField(
                    value = state.query,
                    onValueChange = { viewModel.onSearchChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = {
                        Text(
                            "Search transactions...",
                            color = onSurfVar
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50)
                )

                Spacer(Modifier.height(10.dp))

                // Filters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chips.forEach { chip ->
                        FilterChip(
                            selected = state.selectedFilter == chip,
                            onClick = { viewModel.onFilterChange(chip) },
                            label = { Text(chip) },
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
                Text(
                    text = "No transactions found",
                    color = onSurfVar
                )
            }

        } else {

            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                items(state.filteredTransactions, key = { it.id }) { txn ->

                    HistoryTransactionCard(
                        txn = txn,
                        onDelete = {
                            viewModel.deleteTransaction(txn)
                        }
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryTransactionCard(
    txn: Transaction,
    onDelete: () -> Unit
) {
    val isIncome = txn.type == "income"

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    txn.category,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    txn.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {

                Text(
                    text = if (isIncome)
                        "+₹${txn.amount}"
                    else
                        "-₹${txn.amount}",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isIncome)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.error
                )

                TextButton(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}