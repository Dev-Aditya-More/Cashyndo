package com.example.cashyndoapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cashyndoapp.ui.screens.TransactionUI
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    initialType: String = "Income",
    onSaveClick: (TransactionUI) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // ✅ always expands fully
    )
    var selectedType by rememberSaveable { mutableStateOf(initialType) }

    val types = listOf("Income", "Expense", "Transfer")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            // Chips instead of tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                types.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Form for selected type
            TransactionForm(
                type = selectedType,
                onSaveClick = onSaveClick
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionForm(
    type: String,
    onSaveClick: (TransactionUI) -> Unit
) {
    var amount by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var date by rememberSaveable { mutableStateOf(LocalDate.now().toString()) }
    var notes by rememberSaveable { mutableStateOf("") }

    val categories = when (type) {
        "Income" -> listOf("Salary", "Bonus", "Freelance", "Other")
        "Expense" -> listOf("Groceries", "Bills", "Shopping", "Transport", "Other")
        else -> listOf("Bank Transfer", "Wallet Transfer", "Other")
    }

    OutlinedTextField(
        value = amount,
        onValueChange = { amount = it },
        label = { Text("Amount") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(8.dp))

    // Working expandable category dropdown
    ExposedDropdownMenuBox(
        expanded = categoryMenuExpanded,
        onExpandedChange = { categoryMenuExpanded = it }
    ) {
        OutlinedTextField(
            value = category,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        DropdownMenu(
            expanded = categoryMenuExpanded,
            onDismissRequest = { categoryMenuExpanded = false }
        ) {
            categories.forEach { cat ->
                DropdownMenuItem(
                    text = { Text(cat) },
                    onClick = {
                        category = cat
                        categoryMenuExpanded = false
                    }
                )
            }
        }
    }

    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
        value = date,
        onValueChange = { date = it },
        label = { Text("Date (YYYY-MM-DD)") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(8.dp))

    OutlinedTextField(
        value = notes,
        onValueChange = { notes = it },
        label = { Text("Notes") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(12.dp))

    Button(
        onClick = {
            val amt = amount.toDoubleOrNull() ?: 0.0
            onSaveClick(TransactionUI(category, date, amt, type))
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Save $type")
    }
}
