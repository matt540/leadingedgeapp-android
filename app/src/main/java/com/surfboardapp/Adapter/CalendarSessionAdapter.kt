package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Activity.FirstActivity
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CalendarSession
import com.surfboardapp.Models.MapData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback


class CalendarSessionAdapter(
    val activity: MainActivity, private var sessionList: ArrayList<CalendarSession>
) : RecyclerView.Adapter<CalendarSessionAdapter.MyViewHolder>() {
    lateinit var context: Context

    private lateinit var sessionType: String

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSesstionType: TextView = itemView.findViewById(R.id.tv_sesstion_type)
        var tv_session_time: TextView = itemView.findViewById(R.id.tv_session_time)
        var btn_cancel: Button = itemView.findViewById(R.id.btn_cancel)
        var btn_reschedule: Button = itemView.findViewById(R.id.btn_reschedule)
        var tvSessionDate: TextView = itemView.findViewById(R.id.tv_session_date)
        var tvSessionCost: TextView = itemView.findViewById(R.id.tv_session_cost)
        var tvSessionLocation: TextView = itemView.findViewById(R.id.tv_session_location)
        var relativeSessionDetails: RelativeLayout =
            itemView.findViewById(R.id.relativeSessionDetails)
        var sessionId: String = ""
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.design_recycler_calendar_session, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val calendarSession: CalendarSession = sessionList[position]

        when (calendarSession.sessionType) {
            "normal" -> {
                sessionType = context.resources.getString(R.string.normalLesson)
            }
            "discovery_lesson" -> {
                sessionType = context.resources.getString(R.string.discoveryLesson)
            }
            "discovery_enrollment" -> {
                sessionType = context.resources.getString(R.string.progressionEnrollment)
            }
            "cruiser" -> {
                sessionType = context.resources.getString(R.string.progressionlevel1)
            }
            "explorer" -> {
                sessionType = context.resources.getString(R.string.progressionlevel2)
            }
            "sport" -> {
                sessionType = context.resources.getString(R.string.progressionlevel3)
            }
            "pro" -> {
                sessionType = context.resources.getString(R.string.progressionlevel4)
            }
            else -> {
                sessionType = "Certificate"
            }
        }
        holder.tvSesstionType.text = sessionType
        holder.tvSessionDate.text = calendarSession.sessionDate
        holder.tvSessionCost.text = StringBuilder("$").append(calendarSession.sessionCost)
        holder.tvSessionLocation.text = calendarSession.sessionLocation
        holder.sessionId = calendarSession.sessionId
        holder.tv_session_time.text = calendarSession.start_time

        holder.relativeSessionDetails.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    FirstActivity::class.java
                ).putExtra("notification_room_id", "")
            )
        }

        var new_df =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val date1 = new_df.parse(calendarSession.sessionDate + " " + calendarSession.start_time)
//        val date1 = new_df.parse("2020-04-27 10:11:20")

        var current_cal = Calendar.getInstance()
        val time =
            "" + current_cal.get(Calendar.HOUR_OF_DAY).toString() + ":" + current_cal.get(
                Calendar.MINUTE
            ).toString() + ":" + current_cal.get(
                Calendar.SECOND
            )


        var current_date =
            current_cal.get(Calendar.YEAR)
                .toString() + "-" + (current_cal.get(Calendar.MONTH) + 1).toString() + "-" + current_cal.get(
                Calendar.DAY_OF_MONTH
            ).toString()


        val date2 = new_df.parse(current_date + " " + time)
//        val date2 = new_df.parse("2020-04-26 10:12:20")

        if (date1.compareTo(date2) > 0) {
            val diff: Long = date1.time - date2.time

            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffDays = diff / (24 * 60 * 60 * 1000)

            Log.e("diffDays", "" + diffDays)
            Log.e("diffHours", "" + diffHours)
            if (diffDays >= 1) {

                holder.btn_cancel.visibility = View.VISIBLE

            } else {
                holder.btn_cancel.visibility = View.GONE
            }
        }
        if (date2.compareTo(date1) > 0) {
            val diff: Long = date2.time - date1.time

            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffDays = diff / (24 * 60 * 60 * 1000)

            Log.e("diffDays", "" + diffDays)
            Log.e("diffHours", "" + diffHours)
            if (diffDays >= 1) {

                holder.btn_cancel.visibility = View.VISIBLE

            } else {
                holder.btn_cancel.visibility = View.GONE
            }
        }

        holder.btn_reschedule.setOnClickListener {
            val dialog: Dialog = CustomLoader().loader(context)
            val api: Api = Connection().getCon(context)
            api.get_session_details(
                "Bearer " + Preference().getUserData(context)?.token,
                "" + calendarSession.sessionId

            ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    dialog.dismiss()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    dialog.dismiss()
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Log.e("macro", "" + response.body())
                            val map = MapData(
                                true,
                                response.body()!!.asJsonObject.get("data").asJsonObject.get("location").asJsonObject.get("id").asString,
                                response.body()!!.asJsonObject.get("data").asJsonObject.get("location").asJsonObject.get("name").asString,
                                response.body()!!.asJsonObject.get("data").asJsonObject.get("location").asJsonObject.get("description").asString,
                                response.body()!!.asJsonObject.get("data").asJsonObject.get("location").asJsonObject.get("foils").asString,
                                response.body()!!.asJsonObject.get("data").asJsonObject.get("location").asJsonObject.get("instructor_count").asString,
                                response.body()!!.asJsonObject.get("data").asJsonObject.get("location").asJsonObject.get("telephone").asString,
                                response.body()!!.toString(),
                                response.body()!!.asJsonObject.get("data").asJsonObject.get("location").asJsonObject.get(
                                    "image"
                                ).asString
                            )
                            CustomMethods().openPage(
                                activity,
                                MainActivity().locationFinderInner,
                                map
                            )
                        }
                    }
                    if (response.code() == 401) {
                        CustomMethods().logOutUser(context)
                    }
                }
            })
        }

        holder.btn_cancel.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.resources.getString(R.string.app_name))
            builder.setMessage("Please contact the Affiliate to receive a refund")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton("Initiate Refund") { cdialog, which ->
                val dialog: Dialog = CustomLoader().loader(context)
                val api: Api = Connection().getCon(context)
                api.sessions_cancel(
                    "Bearer " + Preference().getUserData(context)?.token,
                    "" + calendarSession.sessionId

                ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        dialog.dismiss()
                        cdialog.dismiss()
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        dialog.dismiss()

                        if (response.isSuccessful) {
                            if (response.body() != null) {

                                Toast.makeText(
                                    context,
                                    "Your Session is Cancelled Sucessfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                CustomMethods().openPage(activity, MainActivity().calendar)
                                activity.back.visibility = View.GONE
                                activity.menu.visibility = View.VISIBLE
                                (activity).clearSelection()
                                cdialog.dismiss()

                            }
                        }
                        if (response.code() == 401) {
                            cdialog.dismiss()
                            CustomMethods().logOutUser(context)
                        }
                    }
                })

            }

            builder.setNegativeButton("Close") { cdialog, which ->
                cdialog.dismiss()
            }

            builder.show()



        }

    }
}