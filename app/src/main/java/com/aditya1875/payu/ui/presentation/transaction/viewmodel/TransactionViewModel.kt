package com.aditya1875.payu.ui.presentation.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.payu.domain.models.Transaction
import com.aditya1875.payu.domain.usecases.DeleteTransactionUseCase
import com.aditya1875.payu.domain.usecases.GetTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TransactionUiState(
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val query: String = "",
    val selectedFilter: String = "ALL"
)

class TransactionViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionUiState())
    val state: StateFlow<TransactionUiState> = _state

    val totalExpense: StateFlow<Double> =
        _state.map { state ->
            state.transactions
                .filter { it.type == "expense" }
                .sumOf { it.amount }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )

    val totalIncome: StateFlow<Double> =
        _state.map { state ->
            state.transactions
                .filter { it.type == "income" }
                .sumOf { it.amount }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )

    val balance: StateFlow<Double> =
        combine(totalIncome, totalExpense) { income, expense ->
            income - expense
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase().collect { list ->
                _state.value = _state.value.copy(
                    transactions = list,
                    filteredTransactions = applyFilters(
                        list,
                        _state.value.query,
                        _state.value.selectedFilter
                    )
                )
            }
        }
    }

    fun onSearchChange(query: String) {
        _state.value = _state.value.copy(
            query = query,
            filteredTransactions = applyFilters(
                _state.value.transactions,
                query,
                _state.value.selectedFilter
            )
        )
    }

    fun onFilterChange(filter: String) {
        _state.value = _state.value.copy(
            selectedFilter = filter,
            filteredTransactions = applyFilters(
                _state.value.transactions,
                _state.value.query,
                filter
            )
        )
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            deleteTransactionUseCase(transaction)
        }
    }

    private fun applyFilters(
        list: List<Transaction>,
        query: String,
        filter: String
    ): List<Transaction> {

        return list
            .filter {
                it.category.contains(query, ignoreCase = true) ||
                        it.note.contains(query, ignoreCase = true)
            }
            .filter {
                when (filter) {
                    "INCOME" -> it.type == "income"
                    "EXPENSE" -> it.type == "expense"
                    else -> true
                }
            }
    }
}