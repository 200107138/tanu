package com.example.tanu.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.Models.Post
import com.example.tanu.databinding.PostLayoutBinding

class PostsAdapter(private val context: Context, private val list: ArrayList<Post>) :
    RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

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
    }

    override fun getItemCount(): Int {
        return list.size
    }
}