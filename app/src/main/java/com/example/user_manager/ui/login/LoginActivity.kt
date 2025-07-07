package com.example.user_manager.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.user_manager.R
import com.example.user_manager.databinding.ActivityLoginBinding
import com.example.user_manager.ui.base.BaseActivity
import com.example.user_manager.ui.main.MainActivity
import com.example.user_manager.utils.viewBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : BaseActivity() {
    private val binding by viewBinding(ActivityLoginBinding::inflate)
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupLoginButton()
        observeLoginState()
    }

    private fun setupLoginButton() {
        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (validateInput(username, password)) {
                viewModel.login(username, password)
            }
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginState.Loading -> showLoading()
                is LoginViewModel.LoginState.Success -> handleLoginSuccess()
                is LoginViewModel.LoginState.Error -> showError(state.message)
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isBlank()) {
            binding.usernameLayout.error = getString(R.string.error_username_required)
            return false
        }
        if (password.isBlank()) {
            binding.passwordLayout.error = getString(R.string.error_password_required)
            return false
        }
        return true
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonLogin.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.buttonLogin.isEnabled = true
    }

    private fun handleLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showError(message: String) {
        hideLoading()
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
