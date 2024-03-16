package com.example.tanu.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.data.models.Post
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
        holder.binding.postDescription.text = list[position].description
        Glide.with(context).load(list[position].mediaUrl).into(holder.binding.postMedia)
        // Set click listener for comment button
        holder.binding.messageButton.setOnClickListener {

        }
        holder.binding.commentEditText.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}