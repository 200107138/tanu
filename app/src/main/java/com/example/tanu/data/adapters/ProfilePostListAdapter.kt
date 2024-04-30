package com.example.tanu.data.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tanu.data.models.Post
import com.example.tanu.databinding.ItemActivePostBinding
import com.example.tanu.databinding.ItemInactivePostBinding

class ProfilePostListAdapter(
    private val onActivateClickListener: (String) -> Unit,
    private val onDeactivateClickListener: (String) -> Unit
) : ListAdapter<Post, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ACTIVE_POST_VIEW_TYPE) {
            val binding =
                ItemActivePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ActivePostViewHolder(binding, onDeactivateClickListener)
        } else {
            val binding =
                ItemInactivePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            InactivePostViewHolder(binding, onActivateClickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = getItem(position)
        if (getItemViewType(position) == ACTIVE_POST_VIEW_TYPE) {
            (holder as ActivePostViewHolder).bind(post)
        } else {
            (holder as InactivePostViewHolder).bind(post)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val post = getItem(position)
        return if (post.status == "active") {
            ACTIVE_POST_VIEW_TYPE
        } else {
            INACTIVE_POST_VIEW_TYPE
        }
    }

    companion object {
        private const val ACTIVE_POST_VIEW_TYPE = 0
        private const val INACTIVE_POST_VIEW_TYPE = 1
    }

    class ActivePostViewHolder(
        private val binding: ItemActivePostBinding,
        private val onDeactivateClickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.title.text = post.title
            binding.deactivateButton.setOnClickListener {
                onDeactivateClickListener.invoke(post.id)
            }
        }
    }

    class InactivePostViewHolder(
        private val binding: ItemInactivePostBinding,
        private val onActivateClickListener: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.title.text = post.title
            binding.activateButton.setOnClickListener {
                onActivateClickListener.invoke(post.id)
            }
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
}
