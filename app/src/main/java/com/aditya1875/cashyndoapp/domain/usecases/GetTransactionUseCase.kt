package com.aditya1875.cashyndoapp.domain.usecases

import com.aditya1875.cashyndoapp.domain.models.Transaction
import com.aditya1875.cashyndoapp.domain.repository.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getTransactions()
    }
}