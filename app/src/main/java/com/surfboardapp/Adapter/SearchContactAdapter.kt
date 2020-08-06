package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Activity.FirstActivity
import com.surfboardapp.Models.SearchContactModel
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response

class SearchContactAdapter(
    private var searchContactList: ArrayList<SearchContactModel>
) : RecyclerView.Adapter<SearchContactAdapter.MyViewHolder>() {

    lateinit var context: Context


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_name = itemView.findViewById<TextView>(R.id.txt_name)
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
        return searchContactList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var id = searchContactList[position].id
        holder.txt_name.text = searchContactList[position].name

        holder.lin_search.setOnClickListener {

            apicall(id)

        }

    }

    private fun apicall(id: String) {

        val api = Connection().getCon(context)
        api.create_room("Bearer " + Preference().getUserData(context)?.token, id)
            .enqueue(object : javax.security.auth.callback.Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            context.startActivity(
                                Intent(
                                    context,
                                    FirstActivity::class.java
                                ).putExtra("notification_room_id", "")
                            )
                            (context as Activity).finish()

                        }

                    }
                }
            })
    }

}
//id = intent.getStringExtra("id")
