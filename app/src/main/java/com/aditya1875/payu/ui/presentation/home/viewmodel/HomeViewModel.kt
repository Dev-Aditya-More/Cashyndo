package com.aditya1875.payu.ui.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.payu.domain.models.Transaction
import com.aditya1875.payu.domain.usecases.AddTransactionUseCase
import com.aditya1875.payu.domain.usecases.DeleteTransactionUseCase
import com.aditya1875.payu.domain.usecases.GetHomeDataUseCase
import com.aditya1875.payu.domain.usecases.HomeData
import com.aditya1875.payu.domain.usecases.UpdateTransactionUseCase
import com.aditya1875.payu.ui.presentation.home.screens.CardDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeData(0.0, 0.0, 0.0, emptyList()))
    val state: StateFlow<HomeData> = _state

    private val _cardDetails = MutableStateFlow(CardDetails())
    val cardDetails: StateFlow<CardDetails> = _cardDetails.asStateFlow()

    fun saveCardDetails(details: CardDetails) {
        _cardDetails.value = details
        // persist with DataStore or EncryptedSharedPreferences here
    }

    init {
        println("HomeViewModel INIT START")
        observeHomeData()
    }

    private fun observeHomeData() {
        viewModelScope.launch {
            getHomeDataUseCase().collect {
                _state.value = it
            }
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            addTransactionUseCase(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            deleteTransactionUseCase(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            updateTransactionUseCase(transaction)
        }
    }
}