package com.aditya1875.payu.domain.models

data class Transaction(
    val id: Int,
    val amount: Double,
    val category: String,
    val type: String,
    val note: String,
    val timestamp: Long
)