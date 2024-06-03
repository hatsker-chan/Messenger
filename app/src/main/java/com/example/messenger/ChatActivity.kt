package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var currentUserId: String
    private lateinit var otherUserId: String
    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelFactory: ChatViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID) ?: return
        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID) ?: return

        viewModelFactory = ChatViewModelFactory(currentUserId, otherUserId)
        viewModel = ViewModelProvider(this, viewModelFactory)[ChatViewModel::class.java]

        binding.textViewTitle.text

        messagesAdapter = MessagesAdapter(currentUserId)
        binding.recyclerViewMessages.adapter = messagesAdapter

        observeViewModel()

        binding.imageViewSendMessage.setOnClickListener {
            val text = binding.editTextMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            val message = Message(
                text,
                currentUserId,
                otherUserId
            )
            viewModel.sendMessage(message)
        }
    }

    private fun observeViewModel(){
        viewModel.getMessages().observe(this){
            messagesAdapter.messages = it
        }
        viewModel.getError().observe(this){
            if (it != null){
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getMessageSent().observe(this){
            if (it != null && it == true){
                binding.editTextMessage.setText("")
            }
        }
        viewModel.getOtherUser().observe(this){
            val userInfo = "${it.name} ${it.lastname}"
            binding.textViewTitle.text = userInfo
            val bgResId = if (it.online){
                R.drawable.circle_green
            } else {
                R.drawable.circle_red
            }
            val background = ContextCompat.getDrawable(this@ChatActivity, bgResId)
            binding.viewOnlineStatus.background = background
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUserStatus(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUserStatus(false)
    }

    companion object{
        private const val EXTRA_CURRENT_USER_ID = "current_id"
        private const val EXTRA_OTHER_USER_ID = "other_id"

        fun newIntent(context: Context, currentUserId: String, otherUserId: String): Intent {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId)
            intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId)
            return intent
        }
    }
}