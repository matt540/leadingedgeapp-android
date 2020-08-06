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
import com.surfboardapp.Models.CardListModel
import com.surfboardapp.R

class CardListAdapter(
    val activity: MainActivity,
    private var cardList: ArrayList<CardListModel>
) : RecyclerView.Adapter<CardListAdapter.MyViewHolder>() {
    lateinit var context: Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBrandLogo: ImageView = itemView.findViewById(R.id.img_brand_logo)
        val txtAdapterNumber: TextView = itemView.findViewById(R.id.txt_adapter_number)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context

        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.design_recycler_card_detail, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cardListModel: CardListModel = cardList[position]

        if (cardList[position].brand == "Visa") {
            Picasso.get().load(R.drawable.visa).into(holder.imgBrandLogo)
        }
        holder.txtAdapterNumber.text =
            StringBuilder(cardListModel.brand).append(" " + "endingwith" + " " + cardListModel.last4)

    }
}