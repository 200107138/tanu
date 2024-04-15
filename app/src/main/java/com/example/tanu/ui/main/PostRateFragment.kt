// PostCommentFragment.kt
package com.example.tanu.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentPostRateBinding

class PostRateFragment : Fragment() {

    private lateinit var viewModel: PostRateViewModel
    private lateinit var binding: FragmentPostRateBinding
    private lateinit var post: Post
    companion object {
        private const val POST_KEY = "post_key"

        fun newInstance(post: Post?): PostRateFragment {
            val fragment = PostRateFragment()
            val bundle = Bundle()
            bundle.putSerializable(POST_KEY, post)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostRateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        post = arguments?.getSerializable(POST_KEY) as? Post ?: return

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, PostRateViewModelFactory(repository)).get(
            PostRateViewModel::class.java)


        viewModel.commentLiveData.observe(viewLifecycleOwner, Observer {  review ->
            review?.let {
                binding.editText.setText(it.comment)
                binding.seekBar.progress = it.rate-1
// Change ImageView source based on rating
                val drawableResource = when (it.rate) {
                    1 -> R.drawable.one
                    2 -> R.drawable.two
                    3 -> R.drawable.three
                    4 -> R.drawable.four
                    5 -> R.drawable.five
                    else -> R.drawable.three // Use a default image if rate is out of range
                }
                binding.rateImageView.setImageResource(drawableResource)
            }
        })
        viewModel.getReviewByPostIdAndUserId(post.id)
            // Set up seekbar listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val drawableResource = when (progress) {
                    0 -> R.drawable.one
                    1 -> R.drawable.two
                    2 -> R.drawable.three
                    3 -> R.drawable.four
                    4 -> R.drawable.five
                    else -> R.drawable.one // Use a default image if progress is out of range
                }
                binding.rateImageView.setImageResource(drawableResource)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This method is called when the user starts interacting with the seekbar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // This method is called when the user stops interacting with the seekbar
                seekBar?.let {
                    val rate = it.progress+1
                    // Call the ViewModel method to rate the post
                    viewModel.postRate(post.id, rate)

                }
            }
        })
        binding.commentButton.setOnClickListener {
            val commentText = binding.editText.text.toString()
            if (commentText.isNotEmpty()) {
                viewModel.postComment(post.id, commentText)
            }
        }

        viewModel.postCommentLiveData.observe(viewLifecycleOwner, Observer { status ->
            if (status == "success") {
                Toast.makeText(requireContext(), "Comment posted successfully", Toast.LENGTH_SHORT).show()
                binding.commentButton.visibility = View.GONE
                binding.editText.text!!.clear()
                binding.editText.clearFocus()
            } else {
                Toast.makeText(requireContext(), "Failed to post comment", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
