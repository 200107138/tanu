package com.example.tanu.ui.messages

// MessageViewModel.kt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.GetChatRoomIdRequest
import com.example.tanu.data.models.Message
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class MessageViewModel(private val repository: MainRepository) : ViewModel() {

    val chatRoomId: MutableLiveData<String> = MutableLiveData()

    private val _messagesLiveData: MutableLiveData<List<Message>> = MutableLiveData()
    val messagesLiveData: LiveData<List<Message>> = _messagesLiveData

    private val _newMessageLiveData = MutableLiveData<Boolean>()
    val newMessageLiveData: LiveData<Boolean> = _newMessageLiveData

    private val _postInfoLiveData = MutableLiveData<Post>()
    val postInfoLiveData: LiveData<Post> = _postInfoLiveData
    fun getChatRoomId(postId: String, receiverId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getChatRoomId(postId=postId, receiverId=receiverId)
                if (response?.status == "success") {
                    chatRoomId.value = response.chatRoomId
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("MessageViewModel", "Error getting post info: ${e.message}")
            }
        }
    }
    fun getPostInfo(postId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPostInfo(postId)
                if (response.isSuccessful) {
                    _postInfoLiveData.postValue(response.body()?.post)
                } else {
                    // Handle unsuccessful response
                    Log.e("MessageViewModel", "Unsuccessful response: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("MessageViewModel", "Error getting post info: ${e.message}")
            }
        }
    }
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


    fun postMessage(messageText: String, receiverId: String, postId: String) {
            viewModelScope.launch {
                try {
                    val response = repository.postMessage(chatRoomId=chatRoomId.value ?: "", receiverId=receiverId, postId=postId, messageText=messageText)
                    if (response?.status == "success") {
                        Log.e("MessageViewModel", "returned chatRoomId: ${response.chatRoomId}")
                        if(chatRoomId.value == "") {
                            chatRoomId.value = response.chatRoomId
                        }
                        _newMessageLiveData.postValue(true)
                    }
                    else{
                        Log.e("MessageViewModel", response!!.message)
                    }
                } catch (e: Exception) {
                    Log.e("MessageViewModel", "Error getting post info: ${e.message}")
                }
            }
    }
}
