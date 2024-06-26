package com.example.tanu.ui.messages

// MessageActivity.kt
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.MessageAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityMessageBinding
import com.example.tanu.ui.main.PostActivity
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MessageActivity : AppCompatActivity() {

    private lateinit var viewModel: MessageViewModel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var binding: ActivityMessageBinding
    private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel, Adapter, and RecyclerView
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        val viewModelFactory = MessageViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MessageViewModel::class.java)

        val chatRoomId = intent.getStringExtra("chatRoomId") ?: ""
        val postId = intent.getStringExtra("postId") ?: ""
        val receiverId = intent.getStringExtra("receiverId") ?: ""

        setupRecyclerView()
        observe()

        viewModel.chatRoomId.value = chatRoomId
        viewModel.getPostInfo(postId)

        if (postId.isNotEmpty() && receiverId.isNotEmpty()) {
            Log.e("MessageViewModel", "getchatroomid is called")
            viewModel.getChatRoomId(postId=postId, receiverId=receiverId)
        }
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Set up click listener for sendMessageButton
        binding.sendMessageButton.setOnClickListener {
            // Retrieve message text from sendMessageEditText
            val messageText = binding.sendMessageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Call ViewModel's postMessage function
                Log.e("MessageViewModel", receiverId)
                Log.e("MessageViewModel", postId)
                Log.e("MessageViewModel", messageText)
                viewModel.postMessage(receiverId=receiverId, postId = postId, messageText = messageText)
                // Clear the input field after sending message
                binding.sendMessageEditText.text?.clear()
            } else {
                // Handle case where message text is empty
                // You can display a toast or show an error message to the user
            }
        }
        // Set up click listener for openPostButton
        binding.postInfo.setOnClickListener {
            // Start PostActivity and pass postId as an intent extra
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("postId", postId)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(listOf())
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            adapter = messageAdapter
        }
    }

    private fun observe() {
        // Observe postInfoLiveData
        viewModel.postInfoLiveData.observe(this, Observer { post ->
            // Update UI with post info
            post?.let {
                binding.postTitle.text = it.title
                binding.userName.text = it.user.name
                if (it.mediaUrls.isNotEmpty()) {
                    Glide.with(this).load(it.mediaUrls[0]).into(binding.postMedia)
                }
            }
        })
        // Observe chatRoomId LiveData
        viewModel.chatRoomId.observe(this, Observer { chatRoomId ->
            Log.e("MessageViewModel", chatRoomId)
            if (!chatRoomId.isNullOrEmpty()) {
                connectToSocketIO()
                viewModel.getMessages(chatRoomId)
            }
        })
        viewModel.messagesLiveData.observe(this, Observer { messages ->
            messages?.let {
                messageAdapter.messages = messages
                messageAdapter.notifyDataSetChanged()
                // Scroll to the bottom
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
            }
        })
        viewModel.newMessageLiveData.observe(this, Observer { newMessageStatus ->
            newMessageStatus?.let { success ->
                if (success) {
                    // Emit newMessage event with chatRoomId
                    socket.emit("newMessage", viewModel.chatRoomId.value)
                } else {
                    // Handle failure, if needed
                }
            }
        })
    }



    private fun connectToSocketIO() {
        try {
            // Connect to Socket.IO server
            socket = IO.socket("https://api-tanu.onrender.com")
            val accessToken = SessionManager(this).fetchAccessToken()
            socket.emit("connected", viewModel.chatRoomId.value, accessToken)
            // Listen for "newMessage" event
            socket.on("newMessage") { args ->
                Log.e(TAG, "newMessage!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                runOnUiThread {
                    // Update ViewModel with the new message
                    viewModel.getMessages(viewModel.chatRoomId.value!!)
                }
            }
            // Connect to the server
            socket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Disconnect Socket.IO when activity is destroyed
        if (::socket.isInitialized) {
            socket.disconnect()
        }
    }
}
