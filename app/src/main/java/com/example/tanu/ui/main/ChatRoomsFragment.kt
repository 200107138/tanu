package com.example.tanu.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.ChatRoomsAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentChatRoomsBinding
// ConversationsFragment.kt
class ChatRoomsFragment : Fragment() {

    private lateinit var viewModel: ChatRoomsViewModel
    private lateinit var binding: FragmentChatRoomsBinding
    private lateinit var chatRoomsAdapter: ChatRoomsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext()) // Create SessionManager instance
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        val factory = ChatRoomsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ChatRoomsViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        viewModel.getChatRooms()
    }

    private fun setupRecyclerView() {
        chatRoomsAdapter = ChatRoomsAdapter(requireContext()) { chatRoomId ->
            // Handle item click, open MessageActivity with conversationId argument
            val intent = Intent(requireContext(), MessageActivity::class.java).apply {
                putExtra("chatRoomId", chatRoomId)
            }
            startActivity(intent)
        }
        binding.chatRoomsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatRoomsAdapter
        }
    }


    private fun observeViewModel() {
        viewModel.chatRoomsLiveData.observe(viewLifecycleOwner) { chatRooms ->
            chatRooms?.let {
                chatRoomsAdapter.submitList(it)
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.d("chatRoomsFragment", errorMessage)
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
