package com.example.cashyndoapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cashyndoapp.ui.components.AddTransactionBottomSheet
import com.example.cashyndoapp.ui.components.TransactionsFabMenu

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onAddClick: () -> Unit,
    navController : NavController
) {
    val dummyTransactions = listOf(
        TransactionUI("Salary", "2025-08-10", 50000.0, "Income"),
        TransactionUI("Groceries", "2025-08-09", -1200.0, "Expense"),
        TransactionUI("Electricity Bill", "2025-08-07", -800.0, "Expense"),
        TransactionUI("Freelance Project", "2025-08-05", 7500.0, "Income"),
    )

    var showSheet by remember { mutableStateOf(false) }
    var initialType by remember { mutableStateOf("Income") }

    Scaffold(
        floatingActionButton = {
            TransactionsFabMenu(
                onAddIncomeClick = { initialType = "Income" ; showSheet = true },
                onAddExpenseClick = { initialType = "Expense" ; showSheet = true },
                onAddTransferClick = { initialType = "Transfer" ; showSheet = true }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(dummyTransactions) { txn ->
                TransactionItem(txn)
            }
        }
        if (showSheet) {
            AddTransactionBottomSheet(
                initialType = initialType,
                onSaveClick = {
                    Log.d("Transaction", "Saved: $it")
                    showSheet = false
                },
                onDismiss = { showSheet = false }
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(transaction.name, style = MaterialTheme.typography.titleMedium)
                Text(transaction.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                text = "₹${transaction.amount}",
                style = MaterialTheme.typography.titleMedium,
                color = if (transaction.amount >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}

data class TransactionUI(
    val name: String,
    val date: String,
    val amount: Double,
    val type: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TransactionsScreenPreview() {
    TransactionsScreen(onAddClick = {}, navController = NavController(LocalContext.current))
}