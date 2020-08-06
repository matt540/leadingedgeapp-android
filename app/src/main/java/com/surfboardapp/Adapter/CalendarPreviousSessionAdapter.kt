package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.PreviousCalendarSession
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class CalendarPreviousSessionAdapter(
    val activity: MainActivity, private var previousSessionList: ArrayList<PreviousCalendarSession>
) : RecyclerView.Adapter<CalendarPreviousSessionAdapter.MyViewHolder>() {
    lateinit var context: Context
    private lateinit var sessionType: String


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPreviousSessionType: TextView = itemView.findViewById(R.id.tvPreviousSessionType)
        var tvPreviousDate: TextView = itemView.findViewById(R.id.tvPreviousDate)
        var tvPreviousCost: TextView = itemView.findViewById(R.id.tvPreviousCost)
        var tvPreviousLocation: TextView = itemView.findViewById(R.id.tvPreviousLocation)
        var linearClickToSubmit: LinearLayout = itemView.findViewById(R.id.linearClickToSubmit)
        var lin_cancelled: LinearLayout = itemView.findViewById(R.id.lin_cancelled)
        var btn_cancelled: Button = itemView.findViewById(R.id.btn_cancelled)
        var linearRating: LinearLayout = itemView.findViewById(R.id.linearRating)
        var tvClickToSubmit: TextView = itemView.findViewById(R.id.tvClickToSubmit)
        var previousRating: RatingBar =
            itemView.findViewById(R.id.previousRating)
        var sessionId: String = ""
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.design_recycler_calendar_previous_session, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return previousSessionList.size
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val calendarPreviousSession: PreviousCalendarSession = previousSessionList[position]
        when (calendarPreviousSession.previousSessionType) {
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
        holder.tvPreviousSessionType.text = sessionType
        holder.tvPreviousDate.text = calendarPreviousSession.previousSessionDate
        holder.tvPreviousCost.text =
            StringBuilder("$").append(calendarPreviousSession.previousSessionCost)
        holder.tvPreviousLocation.text = calendarPreviousSession.previousSessionLocation
        holder.sessionId = calendarPreviousSession.previousSessionId

        val noOfStar = calendarPreviousSession.previousSessionRating
        val status = calendarPreviousSession.status

        if (status.equals("1")) {
            holder.btn_cancelled.visibility = View.GONE

            if ((noOfStar != "")) {
                holder.linearRating.visibility = View.VISIBLE
                holder.linearClickToSubmit.visibility = View.GONE


                val rating: Float = if (noOfStar == "") {
                    0F
                } else {
                    noOfStar.toFloat()
                }
                holder.previousRating.rating = rating
                holder.previousRating.setIsIndicator(true)

            } else {
                holder.linearRating.visibility = View.GONE
                holder.linearClickToSubmit.visibility = View.VISIBLE
                holder.tvClickToSubmit.setOnClickListener {
                    val builder = Dialog(context)
                    val layoutInflater = LayoutInflater.from(context)
                    val dialog: View =
                        layoutInflater.inflate(R.layout.design_rating_popup, null)
                    val rBar: RatingBar = dialog.findViewById(R.id.rating)
                    val btnSubmit: Button = dialog.findViewById(R.id.btnSubmit)
                    rBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

                        btnSubmit.setOnClickListener {
                            //get ratings
                            getRatingApi(rating, holder.sessionId)
                            builder.dismiss()
                        }
                    }



                    builder.setContentView(dialog)
                    builder.show()
                    builder.setCancelable(false)

                    val imgCloseRatingPopup =
                        dialog.findViewById<ImageView>(R.id.imgCloseRatingPopup)

                    imgCloseRatingPopup.setOnClickListener {
                        builder.dismiss()
                    }
                    Log.e("macro", "123")

                }
            }
        }

        if (status.equals("3")) {
            holder.linearRating.visibility = View.GONE
            holder.linearClickToSubmit.visibility = View.GONE
            holder.btn_cancelled.visibility = View.VISIBLE
        }

    }

    private fun getRatingApi(rating: Float, sessionId: String) {

        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context)
        api.getRating("Bearer " + Preference().getUserData(context)?.token, rating, sessionId)
            .enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    CustomLoader().stopLoader(dialog)
                    if (response.isSuccessful) {
                        if (response.body() != null) {


                            val data = response.body()!!.get("data").asJsonObject
                            val id = data.get("id").asString
//                            val session_id = data.get("session_id").asString
//                            val user_id = data.get("user_id").asString
//                            val ratingInApi = data.get("rating").asString
//                            val created_at = data.get("created_at").asString
//                            val updated_at = data.get("updated_at").asString
//                            if (data.has("deleted_at")) {
//                                if (!data.get("deleted_at").isJsonNull) {
//
//                                    val deleted_at = data.get("deleted_at").asString
//                                }
//                            }
                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context)

                    }
                }
            })
    }
}