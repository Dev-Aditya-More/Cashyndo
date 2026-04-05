package com.aditya1875.cashyndoapp.domain.repository.transaction

import com.aditya1875.cashyndoapp.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(): Flow<List<Transaction>>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)
}