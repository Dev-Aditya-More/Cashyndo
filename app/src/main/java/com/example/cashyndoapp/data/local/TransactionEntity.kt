package com.example.cashyndoapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val type: String, // "income" or "expense"
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
