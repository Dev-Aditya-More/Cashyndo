package com.aditya1875.payu.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aditya1875.payu.data.local.TransactionEntity
import com.aditya1875.payu.data.local.dao.TransactionDao

@Database(
    entities = [TransactionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DB_NAME = "transaction_db"
    }
}