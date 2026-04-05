package com.aditya1875.cashyndoapp.domain.mappers

import com.aditya1875.cashyndoapp.data.local.TransactionEntity
import com.aditya1875.cashyndoapp.domain.models.Transaction

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        category = category,
        type = type,
        note = note,
        timestamp = timestamp
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        category = category,
        type = type,
        note = note,
        timestamp = timestamp
    )
}