package com.example.tanu.data.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.data.Models.Post
import com.example.tanu.databinding.PostLayoutBinding

class PostsAdapter(
    private val context: Context,
    private val list: ArrayList<Post>,
    private val onItemClick: (postId: String, commentText: String) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    inner class PostsViewHolder(val binding: PostLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = PostLayoutBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return PostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.binding.postTitle.text = list[position].postId
        holder.binding.postDescription.text = list[position].title
        Glide.with(context).load(list[position].mediaUrl).into(holder.binding.imageView)
        // Set click listener for comment button
        holder.binding.commentButton.setOnClickListener {
            val postId = list[position].postId
            val commentText = holder.binding.commentEditText.text.toString()
            if (commentText.isNotEmpty()) {
                onItemClick(postId, commentText)
                // Optionally, clear the EditText after commenting
                holder.binding.commentEditText.text.clear()
            } else {
                // Show error message if comment text is empty
                Toast.makeText(context, "Comment text cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}