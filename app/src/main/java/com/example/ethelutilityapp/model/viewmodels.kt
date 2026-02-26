package com.example.ethelutilityapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethelutilityapp.helper.CurrencyRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {
    private val _rates = MutableStateFlow<Map<String, Double>>(emptyMap())
    val rates: StateFlow<Map<String, Double>> = _rates.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchRates("USD")
    }

    fun fetchRates(base: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = CurrencyRetrofit.api.getLatestRates(base)
                _rates.value = response.rates
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to fetch rates: ${e.message}"
                _rates.value = emptyMap()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class SettingsViewModel : ViewModel() {
    private val _backgroundColor = MutableStateFlow(0xFFFFFFFF) // White
    val backgroundColor: StateFlow<Long> = _backgroundColor.asStateFlow()

    private val _isMusicEnabled = MutableStateFlow(false)
    val isMusicEnabled: StateFlow<Boolean> = _isMusicEnabled.asStateFlow()

    private val _language = MutableStateFlow("English")
    val language: StateFlow<String> = _language.asStateFlow()

    fun setBackgroundColor(color: Long) {
        _backgroundColor.value = color
    }

    fun toggleMusic(enabled: Boolean) {
        _isMusicEnabled.value = enabled
    }

    fun setLanguage(lang: String) {
        _language.value = lang
    }
}
