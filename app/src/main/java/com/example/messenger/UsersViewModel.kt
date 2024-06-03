package com.example.messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class UsersViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val user = MutableLiveData<FirebaseUser>()
    private val database = FirebaseDatabase.getInstance()
    private val usersReference = database.getReference("Users")
    private val users = MutableLiveData<List<User>>()

    init {
        auth.addAuthStateListener {
            user.value = it.currentUser
        }

        usersReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = auth.currentUser ?: return
                val usersFromDb: MutableList<User> = ArrayList()
                for (dataSnapshot in snapshot.children){
                    val user = dataSnapshot.getValue(User::class.java) ?: continue
                    if(user.id != currentUser.uid) usersFromDb.add(user)
                }
                users.value = usersFromDb
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getUser(): LiveData<FirebaseUser> {
        return user
    }

    fun getUsers(): LiveData<List<User>>{
        return users
    }

    fun logout() {
        setUserStatus(false)
        auth.signOut()
    }

    fun setUserStatus(isOnline: Boolean){
        val fireBaseUser = auth.currentUser ?: return
        usersReference.child(fireBaseUser.uid).child("online").setValue(isOnline)
    }
}