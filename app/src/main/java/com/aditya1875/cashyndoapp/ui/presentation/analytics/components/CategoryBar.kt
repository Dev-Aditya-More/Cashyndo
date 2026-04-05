package com.aditya1875.cashyndoapp.ui.presentation.analytics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aditya1875.cashyndoapp.domain.usecases.CategorySpending

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CategoryBar(
    item: CategorySpending,
    total: Double
) {

    val fraction = if (total == 0.0) 0f else (item.amount / total).toFloat()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(item.category)
            Text("₹${item.amount}")
        }

        LinearWavyProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
        )
    }
}