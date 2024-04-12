package com.example.tanu.data.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tanu.R
import com.example.tanu.data.models.Post
import com.example.tanu.databinding.ItemPostBinding
import com.example.tanu.ui.main.PostRateFragment
import com.example.tanu.ui.main.PostDescriptionFragment
import com.example.tanu.ui.main.PostDiscussionFragment
import com.google.android.material.tabs.TabLayoutMediator

class PostAdapter(private val context: Context) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val posts: MutableList<Post> = mutableListOf()

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            // Bind data to views
            binding.post = post
            binding.executePendingBindings()

            // Set up ViewPager2 with TabLayout
            val fragments = listOf(
                PostDescriptionFragment.newInstance(post),
                PostDiscussionFragment.newInstance(post),
                PostRateFragment.newInstance(post)
            )
            val adapter = PostFragmentAdapter(context as FragmentActivity, fragments)
            binding.viewPager.adapter = adapter
            binding.viewPager.isUserInputEnabled = false
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Description"
                    1 -> "Forum"
                    2 -> "Rate"
                    else -> ""
                }
            }.attach()
            binding.tabLayout.getTabAt(0)!!.setIcon(R.drawable.outline_description_24)
            binding.tabLayout.getTabAt(1)!!.setIcon(R.drawable.outline_forum_24)
            binding.tabLayout.getTabAt(2)!!.setIcon(R.drawable.baseline_star_outline_24)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    fun setPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }
}
