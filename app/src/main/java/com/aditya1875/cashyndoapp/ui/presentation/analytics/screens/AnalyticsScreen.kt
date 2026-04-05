package com.aditya1875.cashyndoapp.ui.presentation.analytics.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aditya1875.cashyndoapp.ui.presentation.analytics.components.CategoryBar
import com.aditya1875.cashyndoapp.ui.presentation.analytics.components.CategoryRow
import com.aditya1875.cashyndoapp.ui.presentation.analytics.viewmodel.AnalyticsViewModel
import com.aditya1875.cashyndoapp.ui.presentation.home.components.BalanceCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnalyticsScreen(navController: NavController) {

    val viewModel: AnalyticsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BalanceCard(
                    balance = state.totalSpent
                )
            }

            item {
                Text(
                    "Spending by Category",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(state.categoryBreakdown) { item ->

                CategoryRow(item)
            }

            item {
                Text(
                    "Top Categories",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(state.topCategories) { item ->
                CategoryBar(item, state.totalSpent)
            }
        }
    }
}