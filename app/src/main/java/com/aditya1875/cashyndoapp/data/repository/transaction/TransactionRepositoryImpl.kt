package com.aditya1875.cashyndoapp.data.repository.transaction

import com.aditya1875.cashyndoapp.data.local.dao.TransactionDao
import com.aditya1875.cashyndoapp.domain.mappers.toDomain
import com.aditya1875.cashyndoapp.domain.mappers.toEntity
import com.aditya1875.cashyndoapp.domain.models.Transaction
import com.aditya1875.cashyndoapp.domain.repository.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getTransactions().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insert(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.update(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.delete(transaction.toEntity())
    }
}