package com.example.tanu.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tanu.data.models.ChatRoom
import com.example.tanu.databinding.ItemChatRoomBinding

// ConversationsAdapter.kt
class ChatRoomsAdapter(private val context: Context, private val onItemClick: (String) -> Unit) : ListAdapter<ChatRoom, ChatRoomsAdapter.ConversationsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsViewHolder {
        val binding = ItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationsViewHolder, position: Int) {
        val conversation = getItem(position)
        holder.bind(conversation)
    }

    inner class ConversationsViewHolder(private val binding: ItemChatRoomBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val chatRoomId = getItem(position).id
                    onItemClick(chatRoomId)
                }
            }
        }

        fun bind(conversation: ChatRoom) {
            binding.apply {
                // Bind conversation data to views here
                chatRoomId.text = conversation.id

                // Handle other views binding
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }
}
