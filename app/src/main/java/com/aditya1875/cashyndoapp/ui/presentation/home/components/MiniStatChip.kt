package com.aditya1875.cashyndoapp.ui.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MiniStatChip(label: String, positive: Boolean) {

    val bg = if (positive)
        Color.White.copy(alpha = 0.15f)
    else
        Color.White.copy(alpha = 0.1f)

    val icon = if (positive)
        Icons.Outlined.ArrowUpward
    else
        Icons.Outlined.ArrowDownward

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(label, color = Color.White, style = MaterialTheme.typography.labelSmall)
    }
}