package com.example.tanu.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.example.tanu.data.UserHolder
import com.example.tanu.data.models.Message
import com.example.tanu.databinding.ItemRecievedMessageBinding
import com.example.tanu.databinding.ItemSentMessageBinding

class MessageAdapter(var messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            SentMessageViewHolder(
                ItemSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ReceivedMessageViewHolder(
                ItemRecievedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SentMessageViewHolder).setData(messages[position])
        } else {
            (holder as ReceivedMessageViewHolder).setData(messages[position])
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == UserHolder.userId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class SentMessageViewHolder(private val binding: ItemSentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(chatMessage: Message) {
            binding.messageText.text = chatMessage.text
            binding.senderName.text = chatMessage.senderId
        }
    }
    class ReceivedMessageViewHolder(private val binding: ItemRecievedMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(chatMessage: Message) {
            binding.messageText.text = chatMessage.text
            binding.senderName.text = chatMessage.senderId
        }
    }
}