package com.surfboardapp.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.MessageData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.my_message.view.*
import kotlinx.android.synthetic.main.other_message.view.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2

class MessageAdapters(val context: Context, val messages: ArrayList<MessageData>?) :
    RecyclerView.Adapter<MessageViewHolder>() {


    fun addMessage(message: MessageData) {
        messages!!.add(message)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages!!.get(position)

        return if (Preference().getUserData(context)?.id == message.sender_id) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.my_message,
                    parent,
                    false
                )
            )
        } else {
            OtherMessageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.other_message,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages!!.get(position)

        holder.bind(message)

    }

    fun removeItem(position: Int) {
        val api = Connection().getCon(context)
        api.delete_message(
            "Bearer " + Preference().getUserData(context)?.token,
            "" + messages?.get(position)?.id
        )
            .enqueue(object : javax.security.auth.callback.Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            messages!!.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, messages.size)

                        }
                    }
                    if (response.code() == 401) {
                        CustomMethods().logOutUser(context)
                    }

                }
            })


    }


    inner class MyMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtMyMessage
        private var txtMyMessageTime: TextView = view.txtMyMessageTime
        var lin_my_message: LinearLayout = view.lin_my_message


        override fun bind(message: MessageData) {
            var dateFormat = "dd/MM/yyyy hh:mm:ss"
            var createTimeMiliSecond = ((message.created_at).toLong() * 1000)
            getDateTime(createTimeMiliSecond.toString(), dateFormat)
            messageText.text = message.message

//            lin_my_message.setOnLongClickListener {
//
//                val api = Connection().getCon(context)
//                api.delete_message(
//                    "Bearer " + Preference().getUserData(context)?.token,
//                    "" + message.id
//                )
//                    .enqueue(object : javax.security.auth.callback.Callback,
//                        retrofit2.Callback<JsonObject> {
//                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//
//                        }
//
//                        override fun onResponse(
//                            call: Call<JsonObject>,
//                            response: Response<JsonObject>
//                        ) {
//
//                            if (response.isSuccessful) {
//                                if (response.body() != null) {
//
//                                    messages!!.removeAt(position)
//                                    notifyItemRemoved(position)
//                                    notifyItemRangeChanged(position, messages.size)
//
//                                }
//                            }
//                            if (response.code() == 401) {
//                                CustomMethods().logOutUser(context)
//                            }
//
//                        }
//                    })
//
//
//
//                true
//            }

        }

        private fun getDateTime(createdAt: String, dateFormat: String): String {
            val formatter = SimpleDateFormat(dateFormat)

            val calendar: Calendar = Calendar.getInstance()
            val calendar2: Calendar = Calendar.getInstance()

            calendar.timeInMillis = createdAt.toLong()
            val date =
                "" + calendar.get(Calendar.DAY_OF_MONTH).toString() + ":" + calendar.get(Calendar.MONTH).toString() + ":" + calendar.get(
                    Calendar.YEAR
                )

            val date2 =
                "" + calendar2.get(Calendar.DAY_OF_MONTH).toString() + ":" + calendar2.get(Calendar.MONTH).toString() + ":" + calendar2.get(
                    Calendar.YEAR
                )

            Log.e("date", "" + date)
            Log.e("date2", "" + date2)
            val time =
                "" + calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString() + ":" + calendar.get(
                    Calendar.SECOND
                )

            if (date.equals(date2)) {
                txtMyMessageTime.text = time
            } else {
                txtMyMessageTime.text = formatter.format(calendar.time)
            }
            Log.e("times", "" + formatter.format(calendar.time))
//            txtMyMessageTime.text = formatter.format(calendar.time)
            return formatter.format(calendar.time)

        }

    }

    inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtOtherMessage
        private var txtOtherMessageTime: TextView = view.txtOtherMessageTime
        var lin_other_message: LinearLayout = view.lin_other_message


        override fun bind(message: MessageData) {
            var dateFormat = "dd/MM/yyyy hh:mm:ss"
            var createTimeMiliSecond = ((message.created_at).toLong() * 1000)
            getDateTime(createTimeMiliSecond.toString(), dateFormat)
            messageText.text = message.message


        }

        private fun getDateTime(createdAt: String, dateFormat: String): String {
            val formatter = SimpleDateFormat(dateFormat)
//
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar: Calendar = Calendar.getInstance()
            val calendar2: Calendar = Calendar.getInstance()
            calendar.timeInMillis = createdAt.toLong()
//
            val date =
                "" + calendar.get(Calendar.DAY_OF_MONTH).toString() + ":" + calendar.get(Calendar.MONTH).toString() + ":" + calendar.get(
                    Calendar.YEAR
                )

            val date2 =
                "" + calendar2.get(Calendar.DAY_OF_MONTH).toString() + ":" + calendar2.get(Calendar.MONTH).toString() + ":" + calendar2.get(
                    Calendar.YEAR
                )

            Log.e("date", "" + date)
            Log.e("date2", "" + date2)
            val time =
                "" + calendar.get(Calendar.HOUR_OF_DAY).toString() + ":" + calendar.get(Calendar.MINUTE).toString() + ":" + calendar.get(
                    Calendar.SECOND
                )

            if (date.equals(date2)) {
                txtOtherMessageTime.text = time
            } else {
                txtOtherMessageTime.text = formatter.format(calendar.time)
            }
            Log.e("time", "" + formatter.format(calendar.time))
//            txtOtherMessageTime.text = formatter.format(calendar.time)
            return formatter.format(calendar.time)

        }
    }
}

open class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: MessageData) {}
}
