package com.aditya1875.payu.ui.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aditya1875.payu.domain.models.Transaction

@Composable
fun TransactionRow(txn: Transaction) {

    val isIncome = txn.type == "income"

    val amountColor = if (isIncome)
        MaterialTheme.colorScheme.tertiary
    else
        MaterialTheme.colorScheme.error

    val iconBg = if (isIncome)
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
    else
        MaterialTheme.colorScheme.error.copy(alpha = 0.15f)

    val icon = if (isIncome)
        Icons.Outlined.ArrowDownward
    else
        Icons.Outlined.ArrowUpward

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = amountColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = txn.category,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            if (txn.note.isNotBlank()) {
                Text(
                    text = txn.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = if (isIncome)
                    "+₹${txn.amount}"
                else
                    "-₹${txn.amount}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = amountColor
            )
        }
    }
}