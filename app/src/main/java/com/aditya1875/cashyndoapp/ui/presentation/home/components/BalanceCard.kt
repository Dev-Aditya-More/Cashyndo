package com.aditya1875.cashyndoapp.ui.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BalanceCard(balance: Double) {

    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = primary,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                "Total Balance",
                color = onPrimary.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "₹$balance",
                color = onPrimary,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MiniStatChip("Income", true)
                MiniStatChip("Expense", false)
            }
        }
    }
}