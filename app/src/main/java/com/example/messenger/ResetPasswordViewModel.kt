package com.example.messenger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordViewModel: ViewModel() {

    private val auth = Firebase.auth
    private val messageState = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()

    fun getMessageState(): LiveData<Boolean>{
        return messageState
    }

    fun getError(): LiveData<String>{
        return error
    }

    fun resetPassword(email: String){
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            messageState.value = true
        }.addOnFailureListener {
            error.value = it.message
        }
    }
}