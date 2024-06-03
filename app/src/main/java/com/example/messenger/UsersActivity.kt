package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.messenger.databinding.ActivityUsersBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    private lateinit var viewModel: UsersViewModel
    private val usersAdapter = UsersAdapter()
    private val firebaseDataBase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDataBase.getReference("Users")
    private lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID) ?: ""

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usersAdapter.users
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        viewModel = ViewModelProvider(this)[UsersViewModel::class.java]
        observeViewModel()
        binding.recyclerViewUsers.adapter = usersAdapter

        usersAdapter.onUserClickListener = object : UsersAdapter.OnUserClickListener{
            override fun onUserClick(user: User) {
                val intent = ChatActivity.newIntent(this@UsersActivity, currentUserId, user.id)
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel(){
        viewModel.getUser().observe(this){
            if (it == null){
                val intent = LoginActivity.newIntent(this@UsersActivity)
                startActivity(intent)
                finish()
            }
        }
        viewModel.getUsers().observe(this){
            usersAdapter.users = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_logout){
            viewModel.logout()
        }
        return super.onOptionsItemSelected(item)
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
        const val TAG = "UsersActivityTag"
        fun newIntent(context: Context, currentUserId: String): Intent{
            val intent = Intent(context, UsersActivity::class.java)
            intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId)
            return intent
        }
    }
}