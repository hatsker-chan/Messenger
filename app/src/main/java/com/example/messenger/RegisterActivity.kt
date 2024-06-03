package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.ActivityLoginBinding
import com.example.messenger.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        observeViewModel()

        binding.buttonRegister.setOnClickListener {
            val email = getTrimmedValue(binding.editTextNewEmail)
            val password = getTrimmedValue(binding.editTextNewPassword)
            val name = getTrimmedValue(binding.editTextName)
            val lastName = getTrimmedValue(binding.editTextLastName)
            val age = getTrimmedValue(binding.editTextAge)

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()||lastName.isEmpty()||age.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signUp(email, password, name, lastName, age.toInt())
        }
    }

    private fun observeViewModel() {
        viewModel.getUser().observe(this) {
            if (it != null) {
                val intent = UsersActivity.newIntent(this, it.uid)
                startActivity(intent)
                finish()
            }
        }

        viewModel.getError().observe(this) {
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
    private fun getTrimmedValue(editText: EditText): String {
        return editText.text.toString().trim()
    }
}