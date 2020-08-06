package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.DayWiseSession
import com.surfboardapp.Models.PaymentDetails
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList


class DayWiseSessionAdapter(
    val activity: MainActivity,
    private var dayWiseSessionList: ArrayList<DayWiseSession>,
    var payment: PaymentDetails,
    var builderTimeSlot: Dialog
) : RecyclerView.Adapter<DayWiseSessionAdapter.MyViewHolder>() {
    lateinit var context: Context
    private lateinit var call: Call<JsonObject>
    lateinit var type: String
    lateinit var cost: String
    lateinit var date: String
    lateinit var locationId: String
    lateinit var timeSlotId: String
    lateinit var description: String
    lateinit var paymentData: ArrayList<PaymentDetails>


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSessionDate: TextView = itemView.findViewById(R.id.tv_session_date)
        val tvSessionTime: TextView = itemView.findViewById(R.id.tv_session_time)
        val tvSessionDay: TextView = itemView.findViewById(R.id.tv_session_day)
        val linearDaySession: LinearLayout = itemView.findViewById(R.id.linear_day_session)
        val tvSessionCost: TextView = itemView.findViewById(R.id.tv_session_cost)


    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.design_recycler_daywise_session, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dayWiseSessionList.size
    }

    @SuppressLint("DefaultLocale")
    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dayWiseSession: DayWiseSession = dayWiseSessionList[position]
        holder.tvSessionDate.text = dayWiseSession.date

        val s1 = dayWiseSession.from_time
        val s2 = dayWiseSession.to_time
        val f1: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

        val d1: Date = f1.parse(s1)!!
        val f12: DateFormat = SimpleDateFormat("h:mma", Locale.ENGLISH)


        val d2: Date = f1.parse(s2)!!
        val f22: DateFormat = SimpleDateFormat("h:mma", Locale.ENGLISH)
        val difference: Long = d2.time - d1.time
        val seconds: Long = difference / 1000
        val minute = seconds / 60
        val hours = minute / 60
        val days = hours / 24



        holder.tvSessionTime.text =
            (f12.format(d1).toLowerCase() + " - " + f22.format(d2).toLowerCase())
        holder.tvSessionDay.text = dayWiseSession.day.capitalize(Locale.ENGLISH)

        holder.linearDaySession.setOnClickListener {
            builderTimeSlot.dismiss()
            val dialog: Dialog = CustomLoader().loader(context)
            val api: Api = Connection().getCon(context)
            //check booking is allowed or not
            call = api.checkBookingSession("Bearer " + Preference().getUserData(context)?.token)

            call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    CustomLoader().stopLoader(dialog)
                    try {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val data = response.body()!!.get("data").asJsonObject
                                val allowed = data.get("allowed").asBoolean
//                        val message = response.body()!!.get("message").asString
                                type = payment.type
                                if (type == "normal_session") {
                                    cost = minute.toString()
                                } else {

                                    cost = payment.cost
                                }

                                date = payment.date
                                locationId = payment.id
                                timeSlotId = payment.timeSlotId
                                description = payment.description
                                paymentData = ArrayList()
                                val payment = PaymentDetails(
                                    type,
                                    cost,
                                    date,
                                    timeSlotId,
                                    locationId, description,
                                    "", "", "", "", "", ""
                                )
                                paymentData.add(payment)

                                if (allowed) {
                                    CustomMethods().openPagePaymentDetails(
                                        activity,
                                        MainActivity().sessionPaymentFragment, payment
                                    )


                                } else {
                                    Toast.makeText(
                                        context,
                                        "Booking not available",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                        }
                        if (response.code() == 401) {

                            CustomMethods().logOutUser(context)

                        }

                    } catch (e: Exception) {

                    }
                }
            })
        }

        type = payment.type
        cost = payment.cost

        if (type == "normal_session") {
            holder.tvSessionCost.text = StringBuilder("$ ").append(minute)

        } else {

            holder.tvSessionCost.text = cost
        }


    }


}