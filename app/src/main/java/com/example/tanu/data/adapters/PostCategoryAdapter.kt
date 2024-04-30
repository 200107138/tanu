package com.example.tanu.data.adapters
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.data.models.PostCategory
import com.example.tanu.databinding.ItemPostCategoryBinding

class PostCategoryAdapter(
    private val context: Context,
    private val onCategoryClickListener: (MutableMap<String, Boolean>) -> Unit
) : ListAdapter<PostCategory, PostCategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    // List to store the enabled status for each category
    private val categoryStates = mutableMapOf<String, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemPostCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    inner class CategoryViewHolder(private val binding: ItemPostCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.categoryLayout.setOnClickListener {
                val categoryId = getItem(adapterPosition).id
                val currentState = categoryStates[categoryId] ?: false // Get current state, default is false
                val newState = !currentState // Toggle state
                categoryStates[categoryId] = newState // Update state
                onCategoryClickListener(categoryStates) // Pass category ID and new state to click listener
                updateButtonBackground(newState) // Update button background based on the new state
            }
        }

        fun bind(category: PostCategory) {
            binding.categoryName.text = category.name
            // Set the initial background tint based on the initial state
            val initialState = categoryStates[category.id] ?: false
            updateButtonBackground(initialState)

            // Load category image using Glide
            Glide.with(binding.categoryImage)
                .load(category.image)
                .into(binding.categoryImage)
        }

        private fun updateButtonBackground(isEnabled: Boolean) {
            val colorRes = if (isEnabled) R.color.blue else R.color.gray // Define colors for enabled and disabled states
            val backgroundColor = ContextCompat.getColor(context, colorRes) // Get color from resources
            binding.categoryLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor) // Set background tint

            val textColorRes = if (isEnabled) android.R.color.white else android.R.color.black
            binding.categoryName.setTextColor(ContextCompat.getColor(context, textColorRes))
        }
    }


    class CategoryDiffCallback : DiffUtil.ItemCallback<PostCategory>() {
        override fun areItemsTheSame(oldItem: PostCategory, newItem: PostCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostCategory, newItem: PostCategory): Boolean {
            return oldItem == newItem
        }
    }
}
