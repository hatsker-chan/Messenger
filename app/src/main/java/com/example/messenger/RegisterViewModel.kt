package com.example.messenger

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val user = MutableLiveData<FirebaseUser>()
    private val error = MutableLiveData<String>()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("Users")

    init {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                user.value = it.currentUser
            }
        }
    }

    fun getUser(): LiveData<FirebaseUser> {
        return user
    }

    fun getError(): LiveData<String> {
        return error
    }

    fun signUp(
        email: String,
        password: String,
        name: String,
        lastName: String,
        age: Int
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser= it.user ?: return@addOnSuccessListener
                val user = User(firebaseUser.uid, name, lastName, age, false)

                databaseReference.child(user.id).setValue(user)

            }
            .addOnFailureListener {
                error.value = it.message
            }
    }
}