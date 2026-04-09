package com.aditya1875.payu.domain.usecases

import com.aditya1875.payu.domain.models.Transaction
import com.aditya1875.payu.domain.repository.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class HomeData(
    val totalBalance: Double,
    val totalIncome: Double,
    val totalExpense: Double,
    val recentTransactions: List<Transaction>
)

class GetHomeDataUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<HomeData> {
        return repository.getTransactions().map { transactions ->

            println("UseCase running")

            val income = transactions
                .filter { it.type == "income" }
                .sumOf { it.amount }

            val expense = transactions
                .filter { it.type == "expense" }
                .sumOf { it.amount }

            val balance = income - expense

            HomeData(
                totalBalance = balance,
                totalIncome = income,
                totalExpense = expense,
                recentTransactions = transactions.take(5)
            )
        }
    }
}