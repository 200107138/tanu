package com.example.tanu.data.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.models.Post
import com.example.tanu.databinding.ItemDiscussionBinding
import com.example.tanu.ui.forum.DiscussionActivity

class DiscussionListAdapter(private val context: Context) :
    ListAdapter<Discussion, DiscussionListAdapter.DiscussionViewHolder>(DiscussionDiffCallback()) {

    private val discussions: MutableList<Discussion> = mutableListOf()

    inner class DiscussionViewHolder(private val binding: ItemDiscussionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(discussion: Discussion) {
            binding.discussion = discussion
            binding.discussionTitle.text = discussion.title
            binding.discussionDesc.text = discussion.description
            binding.name.text = discussion.user.name
            if(discussion.user.avatarUrl != null){
                Glide.with(binding.root)
                .load(discussion.user.avatarUrl)
                .into(binding.userAvatar)
            }


            binding.root.setOnClickListener {
                // Open DiscussionActivity here
                val intent = Intent(context, DiscussionActivity::class.java)
                // You may need to pass some data to DiscussionActivity
                intent.putExtra("discussion_id", discussion.id)
                context.startActivity(intent)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
        return DiscussionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        val discussion = getItem(position) // Use getItem(position) to get the post at the given position
        holder.bind(discussion)
    }

    fun setDiscussions(discussion: List<Discussion>) {
        submitList(discussion)
    }
}
class DiscussionDiffCallback : DiffUtil.ItemCallback<Discussion>() {
    override fun areItemsTheSame(oldItem: Discussion, newItem: Discussion): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Discussion, newItem: Discussion): Boolean {
        return oldItem == newItem
    }
}