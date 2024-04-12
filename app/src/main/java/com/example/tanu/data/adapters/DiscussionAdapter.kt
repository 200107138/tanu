package com.example.tanu.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.data.models.Discussion
import com.example.tanu.databinding.ItemDiscussionBinding

// PostDiscussionAdapter.kt
class DiscussionAdapter(private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<DiscussionAdapter.PostDiscussionViewHolder>() {

    private val discussions: MutableList<Discussion> = mutableListOf()

    inner class PostDiscussionViewHolder(private val binding: ItemDiscussionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(discussion: Discussion) {
            binding.discussion = discussion
            binding.discussionTitle.text = discussion.title
            binding.email.text = discussion.userEmail
            Glide.with(binding.root)
                .load(discussion.userAvatarUrl)
                .into(binding.userAvatar)
            binding.root.setOnClickListener { onItemClick(discussion.id) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDiscussionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
        return PostDiscussionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostDiscussionViewHolder, position: Int) {
        val discussion = discussions[position]
        holder.bind(discussion)
    }

    override fun getItemCount(): Int = discussions.size

    fun setDiscussions(discussions: List<Discussion>) {
        this.discussions.clear()
        this.discussions.addAll(discussions)
        notifyDataSetChanged()
    }
}
