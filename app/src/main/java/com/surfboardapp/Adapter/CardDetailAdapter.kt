package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Models.CardListModel
import com.surfboardapp.R

class CardDetailAdapter(
    val activity: MainActivity,
    private var cardList: ArrayList<CardListModel>,
    val locationId: String,
    val cost: String,
    private val timeSlot: String,
    val type: String,
    val date: String,
    private val isFromProgrssion: String,
    val sessionId: String,
    val startTime: String,
    val endTime: String,
    val selectedInstructor: String,
    val selectedBoard: String,
    val callback: (CardListModel) -> Unit

) : RecyclerView.Adapter<CardDetailAdapter.MyViewHolder>() {

    lateinit var context: Context
    private lateinit var drawerLayout: DrawerLayout

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        context = parent.context
        drawerLayout = activity.findViewById(R.id.drawer_layout)
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


        holder.linearCardList.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.resources.getString(R.string.app_name))
            builder.setMessage("Confirm Payment? $ " + cost)
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog1, which ->

                callback.invoke(cardList[position])

                dialog1.dismiss()

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.dismiss()
            }

            builder.show()
            //payment intent

        }

    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgBrandLogo: ImageView = itemView.findViewById(R.id.img_brand_logo)
        val txtAdapterNumber: TextView = itemView.findViewById(R.id.txt_adapter_number)
        val linearCardList: LinearLayout = itemView.findViewById(R.id.linear_card_list)


    }

}