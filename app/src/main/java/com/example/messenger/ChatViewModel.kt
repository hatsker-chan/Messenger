package com.example.messenger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatViewModel(private val currentUserId: String, private val otherUserId: String) : ViewModel() {
    private val messages= MutableLiveData<List<Message>>()
    private val otherUser = MutableLiveData<User>()
    private val messageSent= MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val referenceUsers = firebaseDatabase.getReference("Users")
    private val referenceMessages = firebaseDatabase.getReference("Messages")

    init {
        referenceUsers.child(otherUserId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                otherUser.value = user
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        referenceMessages.child(currentUserId).child(otherUserId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesDb = ArrayList<Message>()
                for (dataSnapshot in snapshot.children){
                    val message = dataSnapshot.getValue(Message::class.java) ?: continue
                    messagesDb.add(message)
                }
                messages.value = messagesDb
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getMessages(): LiveData<List<Message>>{
        return messages
    }
    fun getOtherUser(): LiveData<User>{
        return otherUser
    }
    fun getMessageSent(): LiveData<Boolean>{
        return messageSent
    }
    fun getError(): LiveData<String>{
        return error
    }

    fun setUserStatus(isOnline: Boolean){
        referenceUsers.child(currentUserId).child("online").setValue(isOnline)
    }

    fun sendMessage(message: Message){
        referenceMessages
            .child(message.senderId)
            .child(message.receiverId)
            .push().setValue(message)
            .addOnSuccessListener {
                referenceMessages
                    .child(message.receiverId)
                    .child(message.senderId)
                    .push().setValue(message)
                    .addOnSuccessListener {
                        messageSent.value = true
                    }.addOnFailureListener {
                        error.value = it.message
                    }
            }
            .addOnFailureListener {
                error.value = it.message
            }
    }
}

















