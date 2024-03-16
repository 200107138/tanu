package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.GetMessagesResponse
import com.example.tanu.data.models.Message
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch
class MessageViewModel(private val repository: MainRepository) : ViewModel() {

    private val _messagesLiveData: MutableLiveData<List<Message>> = MutableLiveData()
    val messagesLiveData: LiveData<List<Message>> = _messagesLiveData

    private val _newMessageLiveData = MutableLiveData<Boolean>()
    val newMessageLiveData: LiveData<Boolean> = _newMessageLiveData

    fun getMessages(chatRoomId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMessages(chatRoomId)
                if (response?.status == "success") {
                    _messagesLiveData.postValue(response.messages)
                }
            } catch (e: Exception) {
                _messagesLiveData.postValue(emptyList())
            }
        }
    }

    fun postMessage(chatRoomId: String, messageText: String) {
        viewModelScope.launch {
            try {
                val response = repository.postMessage(chatRoomId, messageText)
                if (response?.status == "success") {
                    // Post a Boolean value indicating success
                    _newMessageLiveData.postValue(true)
                }
            } catch (e: Exception) {
                // Handle error
                _newMessageLiveData.postValue(false)
            }
        }
    }

}
