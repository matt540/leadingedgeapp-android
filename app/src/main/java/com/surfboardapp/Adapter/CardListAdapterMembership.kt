package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CardListModelMembership
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class CardListAdapterMembership(
    val activity: MainActivity,
    private var cardList: ArrayList<CardListModelMembership>,
    private var isSame: String
) : RecyclerView.Adapter<CardListAdapterMembership.MyViewHolder>() {
    lateinit var context: Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBrandLogo: ImageView = itemView.findViewById(R.id.img_brand_logo)
        val btn_membership: Button = itemView.findViewById(R.id.btn_membership)
        val txtAdapterNumber: TextView = itemView.findViewById(R.id.txt_adapter_number)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context

        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.design_recycler_card_detail_membership, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cardListModel: CardListModelMembership = cardList[position]

        if (cardListModel.isSame.equals("1")) {
            holder.btn_membership.visibility = View.GONE

        } else {
            holder.btn_membership.visibility = View.VISIBLE
        }
        if (cardList[position].brand.equals("visa")) {
            Picasso.get().load(R.drawable.visa).into(holder.imgBrandLogo)
        }
        holder.txtAdapterNumber.text =
            StringBuilder(cardListModel.brand).append(" " + "endingwith" + " " + cardListModel.last4)

        holder.btn_membership.setOnClickListener {

            val dialog: Dialog = CustomLoader().loader(context)
            val api: Api = Connection().getCon(context)
            api.membership_default(
                "Bearer " + Preference().getUserData(context)?.token,
                "" + cardListModel.id

            ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    dialog.dismiss()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    dialog.dismiss()
                    Log.e("responsemake", "" + response.body())
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            CustomMethods().openPage(activity, MainActivity().membership)
                            activity.back.visibility = View.GONE
                            activity.menu.visibility = View.VISIBLE
                            CustomMethods().hideKeyboard(activity)

                            (activity).clearSelection()
                        }
                    }
                }
            })

        }

    }

}