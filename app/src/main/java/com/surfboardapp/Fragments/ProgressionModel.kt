package com.surfboardapp.Fragments


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.PaymentDetails
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList


class ProgressionModel : Fragment() {
    private lateinit var txtSessionDescriptionProg: TextView
    private lateinit var txtSessionPriceProg: TextView
    private lateinit var btnBookASessionProgression: Button
    private var model: PaymentDetails? = null
    private lateinit var description: String
    private lateinit var price: String
    private var paymentData: ArrayList<PaymentDetails>? = null
    private lateinit var calendar: Calendar
    private lateinit var cc: Calendar
    private lateinit var mHour: String
    private lateinit var mMin: String
    private var currentDay: Int = 0
    private var currentMonth: Int = 0
    private var currentYear: Int = 0
    private lateinit var todayDate: String
    private lateinit var time: String
    private lateinit var tvScreenTitle: TextView
    lateinit var locationId: String
    private lateinit var txtSessionLevel1: TextView
    private lateinit var txtSessionLevel2: TextView
    private lateinit var txtSessionLevel3: TextView
    private lateinit var txtSessionLevel4: TextView

    private lateinit var month: String
    lateinit var date: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progression_enrollment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDetails()
        onClick()
        //for checking of progression level
        progressionLevel()

    }

    private fun progressionLevel() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.progressionLevel("Bearer " + Preference().getUserData(context!!)!!.token, model!!.id)
            .enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    CustomLoader().stopLoader(dialog)
                    try {

                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val data = response.body()!!.get("data").asJsonArray
                                if (data.size() > 0) {
                                    for (i in 0 until data.size()) {
                                        val jsonObj = data.get(i).asJsonObject
                                        val id = jsonObj.get("id").asString
//                                    val locationId1 = jsonObj.get("location_id").asString
//                                    val type = jsonObj.get("type").asString
//                                    val price = jsonObj.get("price").asString
                                    }
                                }
                            }
                        }
                        if (response.code() == 401) {

                            CustomMethods().logOutUser(context!!)

                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show()
                    }
                }
            })

    }

    private fun setDetails() {
        description = model!!.description
        txtSessionDescriptionProg.text = description

        price = model!!.cost
        txtSessionPriceProg.text = StringBuilder("$ ").append(price)

//        if(model!!.type=="progression_level_1"){
        txtSessionLevel1.text = java.lang.StringBuilder("$").append("200")
//        }

//        if(model!!.type=="progression_level_2"){
        txtSessionLevel2.text = java.lang.StringBuilder("$").append("200")
//        }

//        if(model!!.type=="progression_level_3"){
        txtSessionLevel3.text = java.lang.StringBuilder("$").append("200")
//        }

//        if(model!!.type=="progression_level_4"){
        txtSessionLevel4.text = java.lang.StringBuilder("$").append("150")
//        }
    }

    private fun onClick() {
        btnBookASessionProgression.setOnClickListener {
            val payment = PaymentDetails(
                model!!.type,
                price,
                todayDate,
                model!!.timeSlotId,
                model!!.id, model!!.description, "yes", "", "", ""
                , "", ""
            )
            paymentData!!.add(payment)
            CustomMethods().openPagePaymentDetails(
                activity!!,
                MainActivity().sessionPaymentFragment, payment
            )
        }
    }

    private fun init() {
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.progressions)

        model = arguments!!.getSerializable("model") as PaymentDetails?


        txtSessionDescriptionProg = view!!.findViewById(R.id.txt_session_description_prog)
        txtSessionPriceProg = view!!.findViewById(R.id.txt_session_price_prog)
        btnBookASessionProgression = view!!.findViewById(R.id.btnBookASessionProgression)
        txtSessionLevel1 = view!!.findViewById(R.id.txt_session_level1)
        txtSessionLevel2 = view!!.findViewById(R.id.txt_session_level2)
        txtSessionLevel3 = view!!.findViewById(R.id.txt_session_level3)
        txtSessionLevel4 = view!!.findViewById(R.id.txt_session_level4)

        calendar = Calendar.getInstance()

        cc = Calendar.getInstance()

        mHour = cc[Calendar.HOUR_OF_DAY].toString()
        mMin = cc[Calendar.MINUTE].toString()
        paymentData = ArrayList()



        currentYear = calendar[Calendar.YEAR]
        currentMonth = calendar[Calendar.MONTH] + 1
        currentDay = calendar[Calendar.DAY_OF_MONTH]

        month = if (currentMonth < 10) {
            "0$currentMonth"
        } else {
            currentMonth.toString()
        }
        date = if (currentDay < 10) {
            "0$currentDay"
        } else {
            currentDay.toString()
        }



        todayDate = "$month/$date/$currentYear"
        Log.e(
            "Today Date is:",
            todayDate
        )


        time = "$mHour:$mMin:00"


    }


}
