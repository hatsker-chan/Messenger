package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        observeViewModel()
        setupClickListeners()
    }

    private fun observeViewModel() {
        viewModel.getError().observe(this) {
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
        }

        viewModel.getUser().observe(this) {
            if (it != null) {
                val intent = UsersActivity.newIntent(this, it.uid)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextMail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        binding.textViewRegister.setOnClickListener {
            val intent = RegisterActivity.newIntent(this@LoginActivity)
            startActivity(intent)
        }

        binding.textViewForgotPassword.setOnClickListener {
            val intent = ResetPasswordActivity.newIntent(
                this@LoginActivity,
                binding.editTextMail.text.toString()
            )
            startActivity(intent)
        }
    }

    companion object {
        const val TAG = "MainActivityTag"
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}