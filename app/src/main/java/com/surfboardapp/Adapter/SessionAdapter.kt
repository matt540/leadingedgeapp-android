package com.surfboardapp.Adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Models.MapData
import com.surfboardapp.Models.SessionModel
import com.surfboardapp.R
import org.json.JSONObject


class SessionAdapter(

    private var mainActivity: MainActivity,
    private var map: MapData,
    private var sessionList: ArrayList<SessionModel>,
    val callback: (String) -> Unit,
    val callback2: (String) -> Unit,
    val callback3: (String) -> Unit

) : RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    lateinit var context: Context
    private var lastSelectedPosition = -1
    val sessionLists: ArrayList<SessionModel>
    private lateinit var progressionType: String


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionAdapter.ViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_session, null)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    override fun onBindViewHolder(holder: SessionAdapter.ViewHolder, position: Int) {

        if ((sessionList.get(position).type.equals("cruiser")) || (sessionList.get(position).type.equals(
                "explorer"
            )) || (sessionList.get(position).type.equals("sport")) || (sessionList.get(position).type.equals(
                "pro"
            )) || ((sessionList.get(position).type.equals("normal"))) || ((sessionList.get(position).type.equals(
                "certificate"
            )))
        ) {

            when (sessionList.get(position).type) {
                "discovery_enrollment" -> {
                    progressionType = context.resources.getString(R.string.progressionEnrollment)
                }
                "cruiser" -> {
                    progressionType = context.resources.getString(R.string.cruiser)
                }
                "explorer" -> {
                    progressionType = context.resources.getString(R.string.explorer)
                }
                "sport" -> {
                    progressionType = context.resources.getString(R.string.sport)
                }
                "pro" -> {
                    progressionType = context.resources.getString(R.string.pro)
                }
                "certificate" -> {
                    progressionType = context.resources.getString(R.string.certificate)
                }
                "normal" -> {
                    progressionType = "Normal"
                }
                else -> {
                    progressionType = ""
                }
            }

            holder.lin_session_info.visibility = View.INVISIBLE

            holder.txt_adapter_session_title.text =
                "Session Type:" + " " + progressionType
            holder.txt_adapter_session_time.text = sessionList.get(position).description
            holder.txt_adapter_session_fees.text = "$ " + sessionList.get(position).price

            holder.txt_dynamically.text =
                context.resources.getString(R.string.session_new_description)

            holder.radio_session.isChecked = (lastSelectedPosition == position)

        }
        if (sessionList.get(position).type.equals("discovery_lesson")) {

            holder.lin_session_info.visibility = View.VISIBLE
            holder.txt_adapter_session_title.text =
                "Session Type:" + " " + "Discovery Lesson"
            holder.txt_adapter_session_time.text = sessionList.get(position).duration
            holder.txt_adapter_session_fees.text = "$ " + sessionList.get(position).price

            holder.txt_dynamically.text = context.resources.getString(R.string.time)

            holder.radio_session.isChecked = (lastSelectedPosition == position)

            holder.lin_session_info.setOnClickListener {

                val dialog = Dialog(context)
                dialog.setContentView(R.layout.popup_session_description)

                val displayMetrics = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels

                dialog.window?.setLayout(width * 27 / 30, LinearLayout.LayoutParams.WRAP_CONTENT)

                var img_popup_session_close =
                    dialog.findViewById<ImageView>(R.id.img_popup_session_close)
                var popup_session_description =
                    dialog.findViewById<TextView>(R.id.popup_session_description)

                popup_session_description.text = sessionList.get(position).description

                img_popup_session_close.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()

            }

        }


        if (map.isRechedule) {
            holder.radio_session.isEnabled = false
            Log.e("asd", "" + sessionList.get(position).id)
            Log.e(
                "asd", "" + JSONObject(map.response).getJSONObject("data")
                    .getString("session_id")
            )
            if (JSONObject(map.response).getJSONObject("data")
                    .getString("session_id") != "null"
            ) {
                holder.radio_session.isEnabled = false
                if (sessionList.get(position).id == JSONObject(map.response).getJSONObject("data")
                        .getString("session_id")
                ) {
                    holder.radio_session.isChecked = true
                    callback.invoke(sessionLists.get(position).id)
                    callback2.invoke(sessionLists.get(position).duration)
                    callback3.invoke(sessionLists.get(position).price)
                    lastSelectedPosition = position
                    Log.e("selectedId", "" + sessionLists.get(position).id)
                }
            } else {
                holder.radio_session.isChecked = true
                holder.radio_session.isEnabled = false
                callback.invoke(sessionLists.get(position).id)
                callback2.invoke(sessionLists.get(position).duration)
                callback3.invoke(sessionLists.get(position).price)
                lastSelectedPosition = position
                Log.e("selectedId", "" + sessionLists.get(position).id)
            }
        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_adapter_session_title: TextView
        var txt_adapter_session_fees: TextView
        var txt_adapter_session_time: TextView
        var txt_dynamically: TextView
        var radio_session: RadioButton
        var lin_session_info: LinearLayout


        init {
            txt_adapter_session_title =
                itemView.findViewById<View>(R.id.txt_adapter_session_title) as TextView
            txt_adapter_session_fees =
                itemView.findViewById<View>(R.id.txt_adapter_session_fees) as TextView
            txt_adapter_session_time =
                itemView.findViewById<View>(R.id.txt_adapter_session_time) as TextView
            txt_dynamically =
                itemView.findViewById<View>(R.id.txt_dynamically) as TextView
            radio_session = itemView.findViewById<View>(R.id.radio_session) as RadioButton
            lin_session_info = itemView.findViewById<View>(R.id.lin_session_info) as LinearLayout


            radio_session.setOnClickListener {
                callback.invoke(sessionLists.get(position).id)
                callback2.invoke(sessionLists.get(position).duration)
                callback3.invoke(sessionLists.get(position).price)
                lastSelectedPosition = adapterPosition
                notifyDataSetChanged()
                Log.e("selectedId", "" + sessionLists.get(position).id)
            }
        }

    }

    init {
        sessionLists = sessionList
    }
}