package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
        observeViewModel()
        binding.editTextEmail.setText(intent.getStringExtra(EXTRA_EMAIL))

        binding.buttonResetPassword.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            viewModel.resetPassword(email)
        }
    }

    private fun observeViewModel(){
        viewModel.getMessageState().observe(this){
            if (it != null && it == true){
                Toast.makeText(this, "Message successfully sent", Toast.LENGTH_LONG).show()
                val intent = LoginActivity.newIntent(this)
                startActivity(intent)
                finish()
            }
        }

        viewModel.getError().observe(this){
            if(it != null){
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        const val EXTRA_EMAIL = "email"

        fun newIntent(context: Context, email: String): Intent {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }
    }
}