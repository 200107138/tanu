package com.example.tanu.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tanu.data.models.DiscussionComment
import com.example.tanu.databinding.ItemDiscussionCommentBinding

class DiscussionCommentAdapter : RecyclerView.Adapter<DiscussionCommentAdapter.DiscussionCommentViewHolder>() {

    private val discussionComments: MutableList<DiscussionComment> = mutableListOf()

    inner class DiscussionCommentViewHolder(private val binding: ItemDiscussionCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: DiscussionComment) {
            binding.comment = comment
            binding.email.text = comment.userEmail
            binding.discussionCommentText.text = comment.text
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionCommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiscussionCommentBinding.inflate(inflater, parent, false)
        return DiscussionCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscussionCommentViewHolder, position: Int) {
        val comment = discussionComments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int = discussionComments.size

    fun setDiscussionComments(comments: List<DiscussionComment>) {
        discussionComments.clear()
        discussionComments.addAll(comments)
        notifyDataSetChanged()
    }
}
