package com.example.tanu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding.signup.setOnClickListener {
            binding.signup.isEnabled = false
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            sharedViewModel.register(email, password)
        }
        binding.login.setOnClickListener {
            (activity as AuthActivity).navigateToLogin()
        }
        sharedViewModel.registerSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Тіркелу сәтті өтті!", Toast.LENGTH_SHORT).show()
                (activity as AuthActivity).navigateToLogin()

                // Disable signup button to prevent multiple clicks
                binding.signup.isEnabled = false
            } else {
                // Enable signup button if registration fails
                binding.signup.isEnabled = true
            }
        }
        return binding.root
    }


    companion object {
        fun newInstance(sharedViewModel: AuthViewModel) = RegisterFragment(sharedViewModel)
    }
}
