package com.example.tanu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tanu.R
import com.example.tanu.databinding.FragmentLoginBinding

// LoginFragment.kt
// LoginFragment.kt
class LoginFragment(private val sharedViewModel: AuthViewModel) : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.signIn.setOnClickListener {
            binding.signIn.isEnabled = false
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            sharedViewModel.login(email, password)
        }

        binding.register.setOnClickListener {
            (activity as AuthActivity).navigateToRegister()
        }
        sharedViewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Start SuccessRegisterActivity
                (activity as AuthActivity).navigateToMainActivity()
                // Disable signup button to prevent multiple clicks
            } else {
                // Enable signup button if registration fails
                binding.signIn.isEnabled = true
            }
        }
        return binding.root
    }

    companion object {
        fun newInstance(sharedViewModel: AuthViewModel) = LoginFragment(sharedViewModel)
    }
}
