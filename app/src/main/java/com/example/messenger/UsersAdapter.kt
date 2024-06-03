package com.example.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter: RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    var users: List<User> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onUserClickListener: OnUserClickListener? = null



    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val info = itemView.findViewById<TextView>(R.id.textViewUserInfo)
        val onlineStatus = itemView.findViewById<View>(R.id.viewIsOnline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.user_item,
            parent,
            false
        )
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        val info = "${user.name} ${user.lastname}, ${user.age}"
        holder.info.text = info
        val bgResId = if (user.online){
            R.drawable.circle_green
        } else {
            R.drawable.circle_red
        }

        val background = ContextCompat.getDrawable(holder.itemView.context, bgResId)
        holder.onlineStatus.background = background

        holder.itemView.setOnClickListener {
            onUserClickListener?.onUserClick(user)
        }
    }

    interface OnUserClickListener{
        fun onUserClick(user: User)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}