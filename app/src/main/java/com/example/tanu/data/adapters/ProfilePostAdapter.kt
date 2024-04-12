package com.example.tanu.data.adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.data.models.Post
import com.example.tanu.databinding.ItemPostListBinding
import com.example.tanu.ui.main.SinglePostActivity

class ProfilePostAdapter(private val context: Context) :
    ListAdapter<Post, ProfilePostAdapter.ProfilePostViewHolder>(ProfilePostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostListBinding.inflate(inflater, parent, false)
        return ProfilePostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class ProfilePostViewHolder(private val binding: ItemPostListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = binding.root.context
                    val intent = Intent(context, SinglePostActivity::class.java).apply {
                        putExtra("position", position)
                        putExtra("posts", ArrayList(currentList))
                    }
                    context.startActivity(intent)
                }
            }
        }
        fun bind(post: Post) {
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
                post.rating >= 7 -> R.color.green // Green for ratings >= 7
                post.rating > 3 -> R.color.yellow // Yellow for ratings > 3
                else -> R.color.red // Red for ratings <= 0
            }
            binding.thumbnailImageView.setBackgroundResource(ratingColor)

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


    // DiffUtil class for calculating the difference between old and new lists
    private class ProfilePostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id // Assuming Post has an id property
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}
