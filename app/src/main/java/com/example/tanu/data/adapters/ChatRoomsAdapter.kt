package com.example.tanu.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.data.UserHolder
import com.example.tanu.data.models.ChatRoom
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.databinding.ItemChatRoomBinding
import kotlinx.coroutines.launch

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
                    val postId = getItem(position).post.id
                    onItemClick(chatRoomId, postId)
                }
            }
        }

        fun bind(chatroom: ChatRoom) {
            binding.apply {
                val currentUserId = UserHolder.userId
                val otherUserId = if (chatroom.receiver.id == currentUserId) {
                    chatroom.sender.email
                } else {
                    chatroom.receiver.email
                }
                email.text = otherUserId
                chatRoomLastMessage.text = chatroom.lastMessage

                // Set the image sources using Glide
                Glide.with(context)
                    .load(chatroom.post.mediaUrls[0]) // Assuming mediaUrl is a list
                    .into(binding.postAvatar)

                if (chatroom.receiver.id == currentUserId && chatroom.sender.avatarUrl != null) {
                    Glide.with(context)
                        .load(chatroom.sender.avatarUrl)
                        .into(binding.userAvatar)
                } else if (chatroom.receiver.id != currentUserId && chatroom.receiver.avatarUrl != null) {
                    Glide.with(context)
                        .load(chatroom.receiver.avatarUrl)
                        .into(binding.userAvatar)
                }
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
