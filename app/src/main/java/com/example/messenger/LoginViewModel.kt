package com.example.messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val error = MutableLiveData<String>()
    private val user = MutableLiveData<FirebaseUser>()

    init {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                user.value = it.currentUser
            }
        }
    }

    fun getError(): LiveData<String> {
        return error
    }

    fun getUser(): LiveData<FirebaseUser> {
        return user
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
        }.addOnFailureListener {
            error.value = it.message
        }
    }
}