package com.example.tanu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _exampleText = MutableLiveData<String>()
    val exampleText: LiveData<String>
        get() = _exampleText

    init {
        // Initialize exampleText with some initial value
        _exampleText.value = "Hello, this is your HomeFragment!"
    }
}