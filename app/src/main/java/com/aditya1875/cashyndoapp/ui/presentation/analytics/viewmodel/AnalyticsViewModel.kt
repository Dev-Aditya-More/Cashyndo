package com.aditya1875.cashyndoapp.ui.presentation.analytics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.cashyndoapp.domain.usecases.AnalyticsData
import com.aditya1875.cashyndoapp.domain.usecases.GetAnalyticsDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val getAnalyticsDataUseCase: GetAnalyticsDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsData(0.0, emptyList(), emptyList()))
    val state: StateFlow<AnalyticsData> = _state

    init {
        viewModelScope.launch {
            getAnalyticsDataUseCase().collect {
                _state.value = it
            }
        }
    }
}