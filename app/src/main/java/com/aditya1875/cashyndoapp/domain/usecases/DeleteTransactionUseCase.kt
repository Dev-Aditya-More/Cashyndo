package com.aditya1875.cashyndoapp.domain.usecases

import com.aditya1875.cashyndoapp.domain.models.Transaction
import com.aditya1875.cashyndoapp.domain.repository.transaction.TransactionRepository

class DeleteTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteTransaction(transaction)
    }
}