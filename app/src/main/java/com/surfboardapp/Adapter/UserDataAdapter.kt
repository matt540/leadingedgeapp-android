package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.surfboardapp.Activity.ChatActivity
import com.surfboardapp.Models.UsersData
import com.surfboardapp.R

class UserDataAdapter(
    private var usersArrayList: ArrayList<UsersData>,
    private var id: String,
    private var senderId: String
) : RecyclerView.Adapter<UserDataAdapter.MyViewHolder>() {

    lateinit var context: Context


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_name = itemView.findViewById<TextView>(R.id.txt_name)
        var user_image = itemView.findViewById<ImageView>(R.id.user_image)
        var lin_search = itemView.findViewById<LinearLayout>(R.id.lin_search)

    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_search_contact, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return usersArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        var roomId = usersArrayList[position].room_id
        holder.txt_name.text = usersArrayList[position].name

        if (usersArrayList[position].image.equals("")) {
            Picasso.get().load(R.drawable.camera).into(holder.user_image)
        } else {
            Picasso.get().load(usersArrayList[position].image).into(holder.user_image)
        }


        holder.lin_search.setOnClickListener {

            context.startActivity(
                Intent(context, ChatActivity::class.java).putExtra(
                    "roomId",
                    roomId
                ).putExtra("id", id).putExtra(
                    "name",
                    holder.txt_name.text.toString()
                )
            )
            (context as Activity).finish()

        }

    }


}