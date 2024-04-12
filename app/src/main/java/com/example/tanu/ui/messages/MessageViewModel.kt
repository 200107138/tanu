package com.example.tanu.ui.messages

// MessageViewModel.kt
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Message
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class MessageViewModel(private val repository: MainRepository) : ViewModel() {

    val chatRoomId: MutableLiveData<String> = MutableLiveData()
    val postLiveData: MutableLiveData<Post> = MutableLiveData()
    private val _messagesLiveData: MutableLiveData<List<Message>> = MutableLiveData()
    val messagesLiveData: LiveData<List<Message>> = _messagesLiveData

    private val _newMessageLiveData = MutableLiveData<Boolean>()
    val newMessageLiveData: LiveData<Boolean> = _newMessageLiveData

    fun setChatRoomId(chatRoomId: String) {
        this.chatRoomId.value = chatRoomId
    }
    fun setPost(post: Post) {
        postLiveData.value = post
    }
    private val _postInfoLiveData = MutableLiveData<Post>()
    val postInfoLiveData: LiveData<Post> = _postInfoLiveData

    fun getPostInfo(postId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPostInfo(postId)
                if (response!!.status == "success") {
                    _postInfoLiveData.postValue(response.post)
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
    fun getChatRoomId() {
        viewModelScope.launch {
            try {
                val response = repository.getChatRoomId(postLiveData.value!!.userId, postLiveData.value!!.id)
                if (response?.status == "success") {
                    Log.e(ContentValues.TAG, response.chatRoomId)
                    setChatRoomId(response.chatRoomId)
                }
                else{
                    Log.e(ContentValues.TAG, "ыаппкупекпек")
                }
            } catch (e: Exception) {
            }
        }
    }


    fun postMessage(messageText: String) {
        val currentChatRoomId = chatRoomId.value
        if (currentChatRoomId != "") {
            viewModelScope.launch {
                try {
                    val response = repository.postMessage(currentChatRoomId!!, "", "", messageText)
                    if (response?.status == "success") {
                        // Post a Boolean value indicating success
                        _newMessageLiveData.postValue(true)
                    }
                } catch (e: Exception) {
                    // Handle error
                    _newMessageLiveData.postValue(false)
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    val response = repository.postMessage("", postLiveData.value!!.userId, postLiveData.value!!.id, messageText)
                    if (response?.status == "success") {
                        // Set chatRoomId.value to response.chatRoomId
                        chatRoomId.value = response.chatRoomId
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}
