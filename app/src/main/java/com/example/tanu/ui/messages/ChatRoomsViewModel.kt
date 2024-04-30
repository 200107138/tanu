package com.example.tanu.ui.messages

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.ChatRoom
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

// ConversationsViewModel.kt
class ChatRoomsViewModel(private val repository: MainRepository) : ViewModel() {

    private val _chatRoomsLiveData: MutableLiveData<List<ChatRoom>> = MutableLiveData()
    val chatRoomsLiveData: LiveData<List<ChatRoom>> = _chatRoomsLiveData

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun getChatRooms() {
        viewModelScope.launch {
            try {
                val response = repository.getChatRooms()
                if (response.isSuccessful) {
                    val conversationResponse = response.body()
                    conversationResponse?.let {
                            _chatRoomsLiveData.postValue(it.chatRooms)
                    }
                } else {
                    // Handle unsuccessful response
                    _errorLiveData.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending message: ${e.message}")
                _errorLiveData.postValue("Error fetching chat rooms: ${e.message}")
            }
        }
    }

}
