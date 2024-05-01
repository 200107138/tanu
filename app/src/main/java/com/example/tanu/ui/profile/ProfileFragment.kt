package com.example.tanu.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.UserHolder
import com.example.tanu.data.adapters.PostListAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentProfileBinding
import com.example.tanu.ui.auth.AuthActivity
import com.example.tanu.ui.main.LeaderboardActivity
import com.example.tanu.ui.main.PostPostActivity


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = UserHolder.userId
        // Initialize ViewModel
        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(repository)).get(
            ProfileViewModel::class.java)
        binding.activePosts.setOnClickListener {
            openProfilePostListActivity("active")
        }
        binding.editNameButton.setOnClickListener {
            startActivityForResult(Intent(requireContext(), ProfileEditUserNameActivity::class.java), EDIT_USER_NAME_REQUEST_CODE)
        }
        binding.inactivePosts.setOnClickListener {
            openProfilePostListActivity("inactive")
        }
        binding.quit.setOnClickListener {
            // Finish this activity
            requireActivity().finish()
            // Open AuthActivity
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }
        binding.add.setOnClickListener {
            startActivity(Intent(requireContext(), PostPostActivity::class.java))
        }
        // Observe userInfoLiveData
        viewModel.userInfoLiveData.observe(viewLifecycleOwner, Observer { userInfo ->
            userInfo?.let {
                // Update UI with user information
                if (userInfo.user.avatarUrl != null) {
                    Glide.with(this)
                        .load(userInfo.user.avatarUrl)
                        .into(binding.avatar)
                }
                binding.name.text = userInfo.user.name
                binding.email.text = userInfo.user.email
                // Set rating logic here...
            }
        })
        viewModel.getUserInfo(userId!!)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_USER_NAME_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            // Refresh the fragment here, you can call the API again or any other refresh logic
            viewModel.getUserInfo(UserHolder.userId!!)
        }
    }

    companion object {
        const val EDIT_USER_NAME_REQUEST_CODE = 1001
    }
    private fun openProfilePostListActivity(postType: String) {
        val intent = Intent(requireContext(), ProfilePostListActivity::class.java)
        intent.putExtra("postType", postType)
        startActivity(intent)
    }
}
