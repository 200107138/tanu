package com.example.tanu.ui.messages

// MessageActivity.kt
import android.content.ContentValues.TAG
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
        // Get post_id from intent extras
        val postId = intent.getStringExtra("postId") ?: ""

        // Observe postInfoLiveData
        viewModel.postInfoLiveData.observe(this, Observer { post ->
            // Update UI with post info
            post?.let {
                binding.postDescription.text = it.description
                if (it.mediaUrls.isNotEmpty()) {
                    Glide.with(this).load(it.mediaUrls[0]).into(binding.postMedia)
                }
            }
        })

        // Call getPostInfo
        viewModel.getPostInfo(postId)
        setupRecyclerView()
        // Observe chatRoomId LiveData
        viewModel.chatRoomId.observe(this, Observer { chatRoomId ->
            if (!chatRoomId.isNullOrEmpty()) {
                connectToSocketIO()
                observeNewMessage()
                // Fetch messages for the chat room
                viewModel.getMessages(chatRoomId)
            }
        })
        observe()
        // Get chatRoomId from intent extras and set it in ViewModel
        val chatRoomId = intent.getStringExtra("chatRoomId") ?: ""
        viewModel.setChatRoomId(chatRoomId)
        val post: Post? = intent.getSerializableExtra("post") as? Post
        post?.let {
            viewModel.setPost(it)
            viewModel.getChatRoomId()
        }

        // Set up click listener for sendMessageButton
        binding.sendMessageButton.setOnClickListener {
            // Retrieve message text from sendMessageEditText
            val messageText = binding.sendMessageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Call ViewModel's postMessage function
                viewModel.postMessage(messageText)
                // Clear the input field after sending message
                binding.sendMessageEditText.text.clear()
            } else {
                // Handle case where message text is empty
                // You can display a toast or show an error message to the user
            }
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
        viewModel.messagesLiveData.observe(this, Observer { messages ->
            messages?.let {
                messageAdapter.messages = messages
                messageAdapter.notifyDataSetChanged()
                // Scroll to the bottom
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
            }
        })
    }

    private fun observeNewMessage() {
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
            socket = IO.socket("http://10.0.2.2:3002")
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
