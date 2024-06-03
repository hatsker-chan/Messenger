package com.example.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MessagesAdapter(val currentUserId: String): RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    var messages: List<Message> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MessageViewHolder(itemView: View) : ViewHolder(itemView){
        val textViewMessage = itemView.findViewById<TextView>(R.id.textViewMessage)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if(message.senderId == currentUserId) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutResId = if (viewType == VIEW_TYPE_MY_MESSAGE){
            R.layout.my_message_item
        } else {
            R.layout.other_message_item
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layoutResId,
            parent,
            false
        )
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.textViewMessage.text = message.text
    }

    companion object{
        const val VIEW_TYPE_MY_MESSAGE = 1
        const val VIEW_TYPE_OTHER_MESSAGE = 0
    }
}