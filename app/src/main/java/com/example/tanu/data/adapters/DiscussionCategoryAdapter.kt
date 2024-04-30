package com.example.tanu.data.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.data.models.DiscussionCategory
import com.example.tanu.data.models.PostCategory
import com.example.tanu.databinding.ItemDiscussionCategoryBinding

class DiscussionCategoryAdapter(
    private val context: Context,
    private val onCategoryClickListener: (MutableMap<String, Boolean>) -> Unit
) : ListAdapter<DiscussionCategory, DiscussionCategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {
    private val categoryStates = mutableMapOf<String, Boolean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemDiscussionCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    inner class CategoryViewHolder(private val binding: ItemDiscussionCategoryBinding) :
        RecyclerView.ViewHolder(binding.root){

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

        fun bind(category: DiscussionCategory) {
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

    class CategoryDiffCallback : DiffUtil.ItemCallback<DiscussionCategory>() {
        override fun areItemsTheSame(oldItem: DiscussionCategory, newItem: DiscussionCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DiscussionCategory, newItem: DiscussionCategory): Boolean {
            return oldItem == newItem
        }
    }
}
