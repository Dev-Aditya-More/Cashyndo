package com.aditya1875.payu.domain.usecases

import com.aditya1875.payu.domain.models.Transaction
import com.aditya1875.payu.domain.repository.transaction.TransactionRepository

class DeleteTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteTransaction(transaction)
    }
}