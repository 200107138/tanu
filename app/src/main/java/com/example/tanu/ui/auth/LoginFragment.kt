package com.example.tanu.ui.auth

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

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            sharedViewModel.login(email, password)
        }

        binding.registerButton.setOnClickListener {
            (activity as AuthActivity).navigateToRegister()
        }

        return binding.root
    }

    companion object {
        fun newInstance(sharedViewModel: AuthViewModel) = LoginFragment(sharedViewModel)
    }
}
