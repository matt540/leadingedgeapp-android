package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Models.MessageModel
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R

class MessageAdapter(
    val activity: MainActivity,
    private var messageList: ArrayList<MessageModel>
) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    lateinit var context: Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserChat: TextView = itemView.findViewById(R.id.tvUserChat)
        val tvSuperAdminChat: TextView = itemView.findViewById(R.id.tvSuperAdminChat)
        val imgUserProfile: ImageView = itemView.findViewById(R.id.imgUserProfile)
        val imgSuperAdmin: ImageView = itemView.findViewById(R.id.imgSuperAdmin)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.design_recyler_chat, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val messageModel: MessageModel = messageList[position]

        if (messageModel.userId == Preference().getUserData(context)!!.id) {

            holder.tvUserChat.text = messageModel.msg
            holder.tvSuperAdminChat.visibility = View.GONE
            holder.imgSuperAdmin.visibility = View.GONE
        } else {
            holder.tvSuperAdminChat.text = messageModel.msg
            holder.tvUserChat.visibility = View.GONE
            holder.imgUserProfile.visibility = View.GONE
        }

        if (messageModel.userProfile != "") {
            Picasso.get().load(messageModel.userProfile).into(holder.imgUserProfile)
        } else {
            Picasso.get()
                .load(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgUserProfile)
        }
    }
}