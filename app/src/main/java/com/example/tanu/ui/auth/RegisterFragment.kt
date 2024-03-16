package com.example.tanu.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.tanu.R
import com.example.tanu.databinding.FragmentRegisterBinding

// RegisterFragment.kt
class RegisterFragment(private val sharedViewModel: AuthViewModel) : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            sharedViewModel.register(email, password)
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    companion object {
        fun newInstance(sharedViewModel: AuthViewModel) = RegisterFragment(sharedViewModel)
    }
}
