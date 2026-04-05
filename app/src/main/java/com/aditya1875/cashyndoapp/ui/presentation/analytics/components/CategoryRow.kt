package com.aditya1875.cashyndoapp.ui.presentation.analytics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aditya1875.cashyndoapp.domain.usecases.CategorySpending

@Composable
fun CategoryRow(item: CategorySpending) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.category)

        Text("₹${item.amount}")
    }
}