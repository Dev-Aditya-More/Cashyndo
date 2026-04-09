package com.aditya1875.payu.domain.usecases

import com.aditya1875.payu.domain.repository.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class CategorySpending(
    val category: String,
    val amount: Double
)

data class AnalyticsData(
    val totalSpent: Double,
    val categoryBreakdown: List<CategorySpending>,
    val topCategories: List<CategorySpending>
)

class GetAnalyticsDataUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<AnalyticsData> {
        return repository.getTransactions().map { transactions ->

            val expenses = transactions.filter { it.type == "expense" }

            val total = expenses.sumOf { it.amount }

            val grouped = expenses
                .groupBy { it.category }
                .map { (category, list) ->
                    CategorySpending(
                        category,
                        list.sumOf { it.amount }
                    )
                }
                .sortedByDescending { it.amount }

            AnalyticsData(
                totalSpent = total,
                categoryBreakdown = grouped,
                topCategories = grouped.take(4)
            )
        }
    }
}