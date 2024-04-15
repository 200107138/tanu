package com.example.tanu.data.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.data.models.Post
import com.example.tanu.databinding.ItemPostListBinding
import com.example.tanu.ui.main.PostActivity

class PostListAdapter(private val context: Context) :
    ListAdapter<Post, PostListAdapter.PostViewHolder>(PostDiffCallback()) {
    inner class PostViewHolder(private val binding: ItemPostListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = binding.root.context
                    val intent = Intent(context, PostActivity::class.java).apply {
                        putExtra("post", post)
                    }
                    context.startActivity(intent)
                }
            }
            // Bind your post data to the item view here
            binding.post = post

            // Set description text with a limit of 20 characters
            val descText = if (post.description.length > 20) {
                post.description.substring(0, 20) + "..." // Limit to 20 characters
            } else {
                post.description
            }
            binding.desc.text = descText


            // Change background color based on rating
            val ratingColor = when {
                post.rating >= 4 -> ContextCompat.getColor(context, R.color.green) // Green for ratings >= 4
                post.rating > 2 -> ContextCompat.getColor(context, R.color.yellow) // Yellow for ratings > 2
                else -> ContextCompat.getColor(context, R.color.red) // Red for ratings <= 0
            }

            // Set stroke color for ShapeableImageView
            binding.thumbnailImageView.strokeColor = ColorStateList.valueOf(ratingColor)

            // Check if there are media URLs available
            if (post.mediaUrls.isNotEmpty()) {
                // Load the first media URL into the ImageView using Glide or any other image loading library
                Glide.with(binding.root)
                    .load(post.mediaUrls[0]) // Load the first media URL
                    .into(binding.thumbnailImageView) // Set the image to the thumbnail ImageView
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostListBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) // Use getItem(position) to get the post at the given position
        holder.bind(post)
    }
    fun setPosts(posts: List<Post>) {
        submitList(posts)
    }

}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}