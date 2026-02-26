package com.example.ethelutilityapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ethelutilityapp.helper.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//ViewModel: store and manage UI-related data that survives configuration changes, such as screen rotations
class QuoteViewModel : ViewModel() {
    //MutableStateFlow: manage state changes in a reactive way.
    private val _quote = MutableStateFlow("Click button to load quote")
    val quote: StateFlow<String> = _quote //StateFlow: read-only version of MutableStateFlow.
    fun loadQuote() {
        //viewModelScope.launch is to start a Kotlin coroutine within a ViewModel
        // that is automatically canceled when the ViewModel is destroyed (when its onCleared() method is called).
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getQuote()
                _quote.value = result
            } catch (e: Exception) {
                _quote.value = "Error: ${e.message}"
            }
        }
    }
}

class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count
    fun increment() {
        _count.value++
    }
}