package com.example.tanu.ui.main

// PostDescriptionFragment.kt
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.PostMediaAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentPostDescriptionBinding
import com.example.tanu.ui.messages.MessageActivity

class PostDescriptionFragment : Fragment() {

    private var _binding: FragmentPostDescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PostDescriptionViewModel
    private lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, PostDescriptionViewModelFactory(repository)).get(
            PostDescriptionViewModel::class.java)
        post = arguments?.getSerializable(POST_KEY) as? Post ?: return

        // Set description text
        binding.postDesctiption.text = post.description

        // Call the getUserInfo API
        viewModel.getUserInfo(post.userId)

        // Observe the getUserInfo LiveData
        viewModel.userInfoLiveData.observe(viewLifecycleOwner) { response ->
            if (response.status == "success") {
                // Update the UI with user info
                binding.email.text = response.user.email
                Glide.with(this).load(response.user.avatarUrl).into(binding.avatar)
            } else {
                // Handle error
            }
        }


        // Set click listener for message button
        binding.messageButton.setOnClickListener {
            // Create an Intent to start the MessageActivity
            val intent = Intent(requireContext(), MessageActivity::class.java)
            // Put the Post object as an extra in the Intent bundle
            intent.putExtra("post", post)
            intent.putExtra("postId", post.id)
            // Start the MessageActivity
            startActivity(intent)
        }



        val mediaAdapter = PostMediaAdapter(post.mediaUrls)
        binding.mediaViewPager.adapter = mediaAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val POST_KEY = "post_key"

        fun newInstance(post: Post?): PostDescriptionFragment {
            val fragment = PostDescriptionFragment()
            val bundle = Bundle()
            bundle.putSerializable(POST_KEY, post)
            fragment.arguments = bundle
            return fragment
        }
    }
}
