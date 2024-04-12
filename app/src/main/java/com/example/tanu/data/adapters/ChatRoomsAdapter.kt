package com.example.tanu.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.data.models.ChatRoom
import com.example.tanu.databinding.ItemChatRoomBinding

// ConversationsAdapter.kt
class ChatRoomsAdapter(private val context: Context, private val onItemClick: (String, String) -> Unit) : ListAdapter<ChatRoom, ChatRoomsAdapter.ConversationsViewHolder>(DiffCallback()) {

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
                    val postId = getItem(position).postId
                    onItemClick(chatRoomId, postId)
                }
            }
        }

        fun bind(chatroom: ChatRoom) {
            binding.apply {
                // Bind conversation data to views here
                email.text = chatroom.userEmail
                chatRoomLastMessage.text = chatroom.lastMessage
                // Load avatar image
                Glide.with(context)
                    .load(chatroom.userAvatar)
                    .into(avatar)

                Glide.with(context)
                    .load(chatroom.postMediaUrls.firstOrNull())
                    .into(postMedia)
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
