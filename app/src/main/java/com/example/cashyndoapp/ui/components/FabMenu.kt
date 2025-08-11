package com.example.cashyndoapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TransactionsFabMenu(
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAddTransferClick: () -> Unit
) {
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val items = listOf(
        Triple(Icons.Default.AttachMoney, "Add Income", onAddIncomeClick),
        Triple(Icons.Default.ShoppingCart, "Add Expense", onAddExpenseClick),
        Triple(Icons.Default.SyncAlt, "Add Transfer", onAddTransferClick)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButtonMenu(
            modifier = Modifier.align(Alignment.BottomEnd),
            expanded = fabMenuExpanded,
            button = {
                ToggleFloatingActionButton(
                    modifier = Modifier
                        .animateFloatingActionButton(
                            visible = true,
                            alignment = Alignment.BottomEnd
                        ),
                    checked = fabMenuExpanded,
                    containerSize = ToggleFloatingActionButtonDefaults.containerSizeMedium(),
                    onCheckedChange = { fabMenuExpanded = !fabMenuExpanded }
                ) {
                    val imageVector by remember {
                        derivedStateOf {
                            if (checkedProgress > 0.5f) Icons.Default.Close else Icons.Default.Add
                        }
                    }
                    Icon(
                        painter = rememberVectorPainter(imageVector),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .animateIcon(checkedProgress = { checkedProgress })
                    )
                }
            }
        ) {
            items.forEach { (icon, label, clickAction) ->
                FloatingActionButtonMenuItem(
                    onClick = {
                        fabMenuExpanded = false
                        clickAction()
                    },
                    icon = { Icon(icon, contentDescription = null) },
                    text = { Text(text = label) },
                )
            }
        }
    }
}

@Preview
@Composable
fun TransactionsFabMenuPreview() {
    TransactionsFabMenu(
        onAddIncomeClick = {},
        onAddExpenseClick = {},
        onAddTransferClick = {}
    )
}