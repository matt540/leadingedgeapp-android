package com.surfboardapp.Fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.DayWiseSessionAdapter
import com.surfboardapp.Adapter.SessionAdapter
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.*
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList


class LocationFinderInnerFragment : Fragment(),
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    private lateinit var tvScreenTitle: TextView
    private lateinit var telephone: String
    private lateinit var locationId: String
    private lateinit var dateArray: JsonArray
    private lateinit var lat: String
    private lateinit var long: String
    private lateinit var range: String
    private lateinit var tvDescriptionLocationInner: TextView
    private lateinit var tvNoOfInst: TextView
    private lateinit var tvTotalNoOfFoils: TextView
    private lateinit var img_location_inner_bg: ImageView
    private lateinit var tvAddToFavourite: TextView
    private lateinit var txt_progression_type: TextView
    private lateinit var txt_session_duration: TextView
    private lateinit var imgLike: ImageView
    private lateinit var linear_addToFav: LinearLayout
    private var model: MapData? = null
    private lateinit var str: String
    private lateinit var call: Call<JsonObject>
    private lateinit var description: String
    private lateinit var minor_fname: String
    private lateinit var minor_lname: String
    private lateinit var cost: String
    private lateinit var type: String
    private lateinit var webViewDesc: WebView
    private lateinit var tvNoDataAvailable: TextView

    //    private lateinit var txtSessionTitle: TextView
    private lateinit var txtSessionPrice: TextView
    var paymentData: ArrayList<PaymentDetails>? = null
    lateinit var ok: TextView
    lateinit var dialogProgression: View
    private var dayWiseSessionList: ArrayList<DayWiseSession>? = null
    lateinit var adapter: DayWiseSessionAdapter
    lateinit var sessionAdapter: SessionAdapter
    private var dayName: String = ""
    private lateinit var builder: Dialog
    private lateinit var buildernew: Dialog
    private lateinit var builderTimeSlot: Dialog
    lateinit var dialog: View
    lateinit var dialognew: View
    private lateinit var btnApprove: Button
    private lateinit var btn_approve_new: Button
    lateinit var tvDescr: TextView
    lateinit var tv_descr_new: TextView
    lateinit var edt_your_name: EditText
    lateinit var edt_your_last_name: EditText
    lateinit var edt_minor_last_name: EditText
    lateinit var edt_minor_name: EditText
    lateinit var webView: WebView
    private lateinit var spinnerGender: Spinner
    private lateinit var etAge: EditText
    private lateinit var txt_desc: TextView
    private lateinit var etWeight: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioYes: RadioButton
    private lateinit var radioNo: RadioButton
    private lateinit var etExperiencedesc: EditText
    private lateinit var linearExpdesc: LinearLayout
    private var expericenceDescription: String = ""
    private var selectedRadioButtonValue: String = ""
    private var selected_dates: String = ""
    private lateinit var cYear: String
    private var TIME_PICKER_INTERVAL: Int = 30

    private lateinit var txtMonth: TextView
    private lateinit var txt11: TextView
    private lateinit var txt12: TextView
    private lateinit var txt13: TextView
    private lateinit var txt14: TextView
    private lateinit var txt15: TextView
    private lateinit var txt16: TextView
    private lateinit var txt17: TextView

    private lateinit var txt21: TextView
    private lateinit var txt22: TextView
    private lateinit var txt23: TextView
    private lateinit var txt24: TextView
    private lateinit var txt25: TextView
    private lateinit var txt26: TextView
    private lateinit var txt27: TextView

    private lateinit var txt31: TextView
    private lateinit var txt32: TextView
    private lateinit var txt33: TextView
    private lateinit var txt34: TextView
    private lateinit var txt35: TextView
    private lateinit var txt36: TextView
    private lateinit var txt37: TextView

    private lateinit var txt41: TextView
    private lateinit var txt42: TextView
    private lateinit var txt43: TextView
    private lateinit var txt44: TextView
    private lateinit var txt45: TextView
    private lateinit var txt46: TextView
    private lateinit var txt47: TextView

    private lateinit var txt51: TextView
    private lateinit var txt52: TextView
    private lateinit var txt53: TextView
    private lateinit var txt54: TextView
    private lateinit var txt55: TextView
    private lateinit var txt56: TextView
    private lateinit var txt57: TextView

    private lateinit var txt61: TextView
    private lateinit var txt62: TextView
    private lateinit var txt63: TextView
    private lateinit var txt64: TextView
    private lateinit var txt65: TextView
    private lateinit var txt66: TextView
    private lateinit var txt67: TextView

    private lateinit var lin11: LinearLayout
    private lateinit var lin12: LinearLayout
    private lateinit var lin13: LinearLayout
    private lateinit var lin14: LinearLayout
    private lateinit var lin15: LinearLayout
    private lateinit var lin16: LinearLayout
    private lateinit var lin17: LinearLayout

    private lateinit var lin21: LinearLayout
    private lateinit var lin22: LinearLayout
    private lateinit var lin23: LinearLayout
    private lateinit var lin24: LinearLayout
    private lateinit var lin25: LinearLayout
    private lateinit var lin26: LinearLayout
    private lateinit var lin27: LinearLayout

    private lateinit var lin31: LinearLayout
    private lateinit var lin32: LinearLayout
    private lateinit var lin33: LinearLayout
    private lateinit var lin34: LinearLayout
    private lateinit var lin35: LinearLayout
    private lateinit var lin36: LinearLayout
    private lateinit var lin37: LinearLayout

    private lateinit var lin41: LinearLayout
    private lateinit var lin42: LinearLayout
    private lateinit var lin43: LinearLayout
    private lateinit var lin44: LinearLayout
    private lateinit var lin45: LinearLayout
    private lateinit var lin46: LinearLayout
    private lateinit var lin47: LinearLayout


    private lateinit var lin51: LinearLayout
    private lateinit var lin52: LinearLayout
    private lateinit var lin53: LinearLayout
    private lateinit var lin54: LinearLayout
    private lateinit var lin55: LinearLayout
    private lateinit var lin56: LinearLayout
    private lateinit var lin57: LinearLayout


    private lateinit var lin61: LinearLayout
    private lateinit var lin62: LinearLayout
    private lateinit var lin63: LinearLayout
    private lateinit var lin64: LinearLayout
    private lateinit var lin65: LinearLayout
    private lateinit var lin66: LinearLayout
    private lateinit var lin67: LinearLayout
    private var lastCheckedPosition: Int = -1
    private var checkedID: Boolean = false

    private val calendar = Calendar.getInstance()
    private lateinit var monthNumber: String
    private lateinit var monthdate: SimpleDateFormat
    var date = ""
    var d = ""
    lateinit var dialogAvailableSession: View
    lateinit var tvSelectedDate: TextView
    lateinit var txt_available_from_time: TextView
    lateinit var txt_available_to_time: TextView
    lateinit var txt_start_time: TextView
    lateinit var txt_end_time: TextView
    lateinit var lin_selected_time: LinearLayout
    lateinit var spinner_timing: Spinner
    lateinit var tv_selected_date: TextView
    lateinit var tv_duration: TextView
    lateinit var btn_session_popup_submit: Button
    lateinit var lin_instructor_board: LinearLayout
    lateinit var lin_instructor: LinearLayout
    lateinit var spinner_instructor: Spinner
    lateinit var spinner_board: Spinner

    lateinit var rvAvailabletimeslots: RecyclerView
    lateinit var recycler_session_data: RecyclerView
    lateinit var btn_session_continue: Button
    lateinit var idApi: String
    lateinit var fromTime: String
    lateinit var toTime: String
    lateinit var day: String
    lateinit var locationIdApi: String
    private var sessionList: ArrayList<SessionModel>? = null

    private var session_id: String = ""
    private var session_location_id: String = ""
    private var session_types: String = ""
    private var session_title: String = ""
    private var session_description: String = ""
    private var session_price: String = ""
    private var session_duration: String = ""
    private var session_created_at: String = ""
    private var session_updated_at: String = ""
    private var session_deleted_at: String = ""
    private var instructor_id: String = ""

    private var selected_it: String = ""
    private var selected_duration: String = ""
    private var selected_price: String = ""

    private var activehours_id: String = ""
    private var activehours_location_id: String = ""
    private var activehours_day: String = ""
    private var activehours_start_time: String = ""
    private var activehours_end_time: String = ""
    private var activehours_available: String = ""
    private var activehours_created_at: String = ""
    private var activehours_updated_at: String = ""
    private var activehours_deleted_at: String = ""
    private var sessionDate: String = ""
    private var start_time_24: String = ""
    private var end_time_24: String = ""
    var jarray_instructor: JsonArray? = null
    var jarray_boards: JsonArray? = null

    private var cal = Calendar.getInstance()
    private var calendar1 = Calendar.getInstance()
    private var calendar3 = Calendar.getInstance()
    private var calendar2 = Calendar.getInstance()

    lateinit var map: MapData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        map = bundle?.getSerializable("model") as MapData
        return inflater.inflate(R.layout.fragment_location_finder_inner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getGenderSpinner()
        onClick()
        setBottomSelection()
        //location session data
        locationSessionDataApi()

    }

    private fun setBottomSelection() {
        val dashboardImage = activity!!.findViewById<ImageView>(R.id.image_location)
        val dashboardText = activity!!.findViewById<TextView>(R.id.text_location)
        dashboardText?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        dashboardImage?.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
    }

    private fun setDescFromApi() {
        val dialog: Dialog = CustomLoader().loader(context)

        Log.e("locationIdnew", "" + locationId)
        val api: Api = Connection().getCon(context!!)
        api.customerWaiver("Bearer " + Preference().getUserData(context!!)!!.token, locationId)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    CustomLoader().stopLoader(dialog)
                    Log.e("responsedes", "" + response.body())
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val data = response.body()!!.get("data").asJsonObject

                            val show = data.get("show").asString
                            if (show.equals("true")) {

                                var type = data.get("type").asString
                                if (type.equals("text")) {
                                    buildernew.setCancelable(false)
                                    buildernew.setContentView(dialognew)
                                    buildernew.show()

                                    val wavier = data.get("wavier").asString
                                    tv_descr_new.visibility = View.VISIBLE
                                    webView.visibility = View.GONE
                                    tv_descr_new.text = wavier

                                } else {

                                    buildernew.setCancelable(false)
                                    buildernew.setContentView(dialognew)
                                    buildernew.show()

                                    tv_descr_new.visibility = View.GONE
                                    webView.visibility = View.VISIBLE

                                    val wavier = data.get("wavier").asString
                                    webView.webViewClient = object : WebViewClient() {
                                        override fun shouldOverrideUrlLoading(
                                            view: WebView?,
                                            url: String?
                                        ): Boolean {
                                            view?.loadUrl(url)
                                            return true
                                        }
                                    }
                                    webView.settings.javaScriptEnabled = true
                                    webView.loadUrl(wavier)
//                                    webView.loadUrl("https://www.google.com")

                                }



                                btn_approve_new.setOnClickListener {

                                    if (edt_your_name.text.toString().trim().equals("")) {

                                        Toast.makeText(
                                            context,
                                            "Please Enter Your Name First",
                                            Toast.LENGTH_LONG
                                        ).show()

                                    } else if (edt_your_last_name.text.toString().trim()
                                            .equals("")
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Please Enter Last Name First",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {

                                        if (edt_minor_name.text.toString().trim().equals("")) {
                                            minor_fname = ""
                                        } else {
                                            minor_fname = edt_minor_name.text.toString()
                                        }
                                        if (edt_minor_last_name.text.toString().trim().equals("")) {
                                            minor_lname = ""
                                        } else {
                                            minor_lname = edt_minor_last_name.text.toString()
                                        }

                                        wavierAcceptedNew(
                                            edt_your_name.text.toString(),
                                            minor_fname,
                                            edt_your_last_name.text.toString(),
                                            minor_lname
                                        )
                                        buildernew.dismiss()
                                    }


                                }
                            }


                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }
                }
            })
    }

    private fun wavierAcceptedNew(
        yourName: String,
        minorName: String,
        lastname: String,
        minorLastName: String
    ) {
        val api: Api = Connection().getCon(context!!)
        api.customerWaiverAccept(
            "Bearer " + Preference().getUserData(context!!)!!.token,
            locationId, yourName, minorName, lastname, minorLastName
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("buildernewacceptre", "" + response.body())
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Log.e("buildernewaccept", "buildernewaccept")

                        }
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun onClick() {

        img_right_arrow.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            displayDate(calendar)
        }
        img_left_arrow.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            displayDate(calendar)
        }
        linear_addToFav.setOnClickListener {

            if (telephone.equals("")) {

                Toast.makeText(context, "No Phone Number Available", Toast.LENGTH_LONG).show()

            } else {
                var phone: String = telephone
                var intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }


        }



        activity!!.back.setOnClickListener {
            if (map.isRechedule) {
                CustomMethods().openPage(activity!!, MainActivity().calendar)
            } else {
                CustomMethods().openPage(activity!!, MainActivity().locationFinder)
            }

        }

        btnApprove.setOnClickListener {

            wavierAccepted()
            builder.dismiss()
        }


    }

    private fun locationSessionDataApi() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        Log.e("locationId", "" + locationId)
        call = api.locationSessiondata(
            "Bearer " + Preference().getUserData(context!!)?.token,
            locationId
        )
        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            @SuppressLint("InflateParams")
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                try {
                    if (response.isSuccessful) {

                        if (response.body() != null) {
                            Log.e("responseInnter", "" + response.body())
                            val data: JsonObject = response.body()!!.get("data").asJsonObject
                            type = data.get("type").asString

                            if (type.equals("discovery_lesson")) {
                                txt_progression_type.text =
                                    context!!.resources.getString(R.string.discoveryLesson)
                            }
                            if (type.equals("cruiser")) {
                                txt_progression_type.text =
                                    context!!.resources.getString(R.string.cruiser)
                            }
                            if (type.equals("explorer")) {
                                txt_progression_type.text =
                                    context!!.resources.getString(R.string.explorer)
                            }
                            if (type.equals("sport")) {
                                txt_progression_type.text =
                                    context!!.resources.getString(R.string.sport)
                            }
                            if (type.equals("pro")) {
                                txt_progression_type.text =
                                    context!!.resources.getString(R.string.pro)
                            }
                            if (type.equals("normal")) {
                                txt_progression_type.text = "Normal"
                            }
                            if (type.equals("certificate")) {
                                txt_progression_type.text = "Certificate"
                            }




                            if (type.equals("discovery_lesson")) {
                                val session = data.get("session").asJsonArray
                                if (session.size() > 0) {
                                    sessionList = ArrayList()

                                    for (i in 0 until session.size()) {

                                        val jsonObj = session.get(i).asJsonObject

                                        if (jsonObj.get("id").isJsonNull) {
                                            session_id = ""
                                        } else {
                                            session_id = jsonObj.get("id").asString
                                        }

                                        if (jsonObj.get("location_id").isJsonNull) {
                                            session_location_id = ""
                                        } else {
                                            session_location_id =
                                                jsonObj.get("location_id").asString
                                        }

                                        if (jsonObj.get("type").isJsonNull) {
                                            session_types = ""
                                        } else {
                                            session_types = jsonObj.get("type").asString
                                        }

                                        if (jsonObj.get("title").isJsonNull) {
                                            session_title = ""
                                        } else {
                                            session_title = jsonObj.get("title").asString
                                        }

                                        if (jsonObj.get("description").isJsonNull) {
                                            session_description = ""
                                        } else {
                                            session_description =
                                                jsonObj.get("description").asString
                                        }

                                        if (jsonObj.get("price").isJsonNull) {
                                            session_price = ""
                                        } else {
                                            session_price = jsonObj.get("price").asString
                                        }


                                        if (jsonObj.get("duration").isJsonNull) {
                                            session_duration = ""
                                        } else {
                                            session_duration = jsonObj.get("duration").asString
                                        }

                                        if (jsonObj.get("created_at").isJsonNull) {
                                            session_created_at = ""
                                        } else {
                                            session_created_at = jsonObj.get("created_at").asString
                                        }

                                        if (jsonObj.get("updated_at").isJsonNull) {
                                            session_updated_at = ""
                                        } else {
                                            session_updated_at = jsonObj.get("updated_at").asString
                                        }

                                        if (jsonObj.get("deleted_at").isJsonNull) {
                                            session_deleted_at = ""
                                        } else {
                                            session_deleted_at = jsonObj.get("deleted_at").asString
                                        }

                                        val sessionModel = SessionModel(
                                            session_id,
                                            session_location_id,
                                            session_types,
                                            session_title,
                                            session_description,
                                            session_price,
                                            session_duration,
                                            session_created_at,
                                            session_updated_at,
                                            session_deleted_at
                                        )
                                        sessionList!!.add(sessionModel)
                                        sessionAdapter = SessionAdapter(activity as MainActivity,
                                            map,
                                            sessionList!!,
                                            { selected_it = it },
                                            { selected_duration = it }, {
                                                selected_price = it
                                            })
                                    }
                                    Log.e("selected_it", "" + selected_it)
                                    recycler_session_data.adapter = sessionAdapter
                                    recycler_session_data.layoutManager =
                                        LinearLayoutManager(context)

                                    sessionAdapter.notifyDataSetChanged()
                                    txt_session_duration.text = "Variable Time"
                                }

                            }

                            if (type.equals("discovery_enrollment")) {


                                var jsonObject_data = response.body()!!.get("data").asJsonObject
                                var jsonObject_session = jsonObject_data.get("session").asJsonObject

                                session_id = jsonObject_session.get("id").asString
                                session_location_id = jsonObject_session.get("location_id").asString
                                session_types = jsonObject_session.get("type").asString
                                session_title = jsonObject_session.get("title").asString
                                session_description = jsonObject_session.get("description").asString
                                session_price = jsonObject_session.get("price").asString
                                session_duration = jsonObject_session.get("duration").asString
                                session_created_at = jsonObject_session.get("created_at").asString
                                session_updated_at = jsonObject_session.get("updated_at").asString

                                if (jsonObject_session.get("deleted_at").isJsonNull) {
                                    session_deleted_at = ""
                                } else {
                                    session_deleted_at =
                                        jsonObject_session.get("deleted_at").asString
                                }


                                val builder = Dialog(context!!)
                                val layoutInflater = LayoutInflater.from(context)
                                dialogProgression =
                                    layoutInflater.inflate(
                                        R.layout.progression_enrollment_popup,
                                        null
                                    )
                                val tvSessionDesc =
                                    dialogProgression.findViewById<TextView>(R.id.tv_session_desc)
//                            val sessionCost =
//                                dialogProgression.findViewById<TextView>(R.id.session_cost_popUp)
//                            val sessionType =
//                                dialogProgression.findViewById<TextView>(R.id.session_type)

                                ok = dialogProgression.findViewById(R.id.ok)
                                tvSessionDesc.setText(R.string.desc_progression)
//                            sessionCost.setText(StringBuilder("$").append(cost))
//                            sessionType.text = type
                                builder.setCancelable(false)
                                builder.setContentView(dialogProgression)
                                builder.show()

                                var calendar: Calendar = Calendar.getInstance()
                                cYear = calendar.get(Calendar.YEAR).toString()

                                monthNumber =
                                    checkDigit(Integer.valueOf((calendar.get(Calendar.MONTH) + 1).toString()))


                                val day = calendar.get(Calendar.DAY_OF_MONTH)

                                Log.e("current_date", "" + monthNumber + "/" + day + "/" + cYear)
                                var current_date = monthNumber + "/" + day + "/" + cYear

                                ok.setOnClickListener {

                                    builder.cancel()
                                    builder.dismiss()
                                    val payment = PaymentDetails(
                                        "" + type,
                                        "" + session_price,
                                        "" + current_date, "",
                                        "" + session_location_id, session_description, "yes",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""

                                    )


                                    paymentData!!.add(payment)
                                    CustomMethods().openPagePaymentDetails(
                                        activity!!,
                                        MainActivity().progressionsFragment,
                                        payment
                                    )
                                }
                            }

                            if ((type.equals("cruiser")) || (type.equals("explorer")) || (type.equals(
                                    "sport"
                                )) || (type.equals(
                                    "pro"
                                ))
                            ) {

                                txt_session_duration.text = "2 hours"

                                val session = data.get("session").asJsonObject
                                sessionList = ArrayList()


                                if (session.get("id").isJsonNull) {
                                    session_id = ""
                                } else {
                                    session_id = session.get("id").asString
                                }

                                if (session.get("location_id").isJsonNull) {
                                    session_location_id = ""
                                } else {
                                    session_location_id = session.get("location_id").asString
                                }

                                if (session.get("type").isJsonNull) {
                                    session_types = ""
                                } else {
                                    session_types = session.get("type").asString
                                }

                                if (session.get("title").isJsonNull) {
                                    session_title = ""
                                } else {
                                    session_title = session.get("title").asString
                                }

                                if (session.get("description").isJsonNull) {
                                    session_description = ""
                                } else {
                                    session_description = session.get("description").asString
                                }

                                if (session.get("price").isJsonNull) {
                                    session_price = ""
                                } else {
                                    session_price = session.get("price").asString
                                }


                                if (session.get("duration").isJsonNull) {
                                    session_duration = ""
                                } else {
                                    session_duration = session.get("duration").asString
                                }

                                if (session.get("created_at").isJsonNull) {
                                    session_created_at = ""
                                } else {
                                    session_created_at = session.get("created_at").asString
                                }

                                if (session.get("updated_at").isJsonNull) {
                                    session_updated_at = ""
                                } else {
                                    session_updated_at = session.get("updated_at").asString
                                }

                                if (session.get("deleted_at").isJsonNull) {
                                    session_deleted_at = ""
                                } else {
                                    session_deleted_at = session.get("deleted_at").asString
                                }

                                val sessionModel = SessionModel(
                                    session_id,
                                    session_location_id,
                                    session_types,
                                    session_title,
                                    session_description,
                                    session_price,
                                    session_duration,
                                    session_created_at,
                                    session_updated_at,
                                    session_deleted_at
                                )
                                sessionList!!.add(sessionModel)
                                sessionAdapter = SessionAdapter(activity as MainActivity,
                                    map,
                                    sessionList!!,
                                    { selected_it = it },
                                    { selected_duration = it }, {
                                        selected_price = it
                                    })


                                recycler_session_data.adapter = sessionAdapter
                                recycler_session_data.layoutManager =
                                    LinearLayoutManager(context)

                                sessionAdapter.notifyDataSetChanged()

                            }

                            if (type.equals("normal")) {

                                val dialog: Dialog = CustomLoader().loader(context)
                                val api: Api = Connection().getCon(context!!)
                                api.checkSubscription(
                                    "Bearer " + Preference().getUserData(context!!)?.token
                                ).enqueue(object : retrofit2.Callback<JsonObject> {
                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        dialog.dismiss()
                                    }

                                    override fun onResponse(
                                        call: Call<JsonObject>,
                                        response: Response<JsonObject>
                                    ) {
                                        dialog.dismiss()
                                        Log.e("responseBooking", "" + response.body())
                                        try {

                                            if (response.isSuccessful) {
                                                if (response.body() != null) {

                                                    var datas =
                                                        response.body()!!.get("data").asJsonObject
                                                    var membership =
                                                        datas.get("membership").asString
                                                    if (membership.equals("false")) {

                                                        val builder = AlertDialog.Builder(context)
                                                        //set title for alert dialog
                                                        builder.setTitle(R.string.app_name)
                                                        //set message for alert dialog
                                                        builder.setMessage("For Normal Sessions Booking User Need to Purchase Membership Subscription.Do you want to continue?")

                                                        //performing positive action
                                                        builder.setPositiveButton("Yes") { dialogInterface, which ->
                                                            dialogInterface.dismiss()
                                                            CustomMethods().openPage(
                                                                activity!!,
                                                                MainActivity().normalSession
                                                            )
                                                            activity!!.back.visibility = View.GONE
                                                            activity!!.menu.visibility =
                                                                View.VISIBLE
                                                            (activity as MainActivity).clearSelection()
                                                        }

                                                        //performing negative action
                                                        builder.setNegativeButton("No") { dialogInterface, which ->
                                                            dialogInterface.dismiss()
                                                            CustomMethods().openPage(
                                                                activity!!,
                                                                MainActivity().dashboard
                                                            )
                                                            activity!!.back.visibility = View.GONE
                                                            activity!!.menu.visibility =
                                                                View.VISIBLE
                                                            (activity as MainActivity).clearSelection()
                                                        }
                                                        // Create the AlertDialog
                                                        val alertDialog: AlertDialog =
                                                            builder.create()
                                                        // Set other dialog properties
                                                        alertDialog.setCancelable(false)
                                                        alertDialog.show()


                                                    } else {
                                                        txt_session_duration.text = "Variable Time"
                                                        val session =
                                                            data.get("session").asJsonObject
                                                        sessionList = ArrayList()


                                                        if (session.get("id").isJsonNull) {
                                                            session_id = ""
                                                        } else {
                                                            session_id = session.get("id").asString
                                                        }

                                                        if (session.get("location_id").isJsonNull) {
                                                            session_location_id = ""
                                                        } else {
                                                            session_location_id =
                                                                session.get("location_id").asString
                                                        }

                                                        if (session.get("type").isJsonNull) {
                                                            session_types = ""
                                                        } else {
                                                            session_types =
                                                                session.get("type").asString
                                                        }

                                                        if (session.get("title").isJsonNull) {
                                                            session_title = ""
                                                        } else {
                                                            session_title =
                                                                session.get("title").asString
                                                        }

                                                        if (session.get("description").isJsonNull) {
                                                            session_description = ""
                                                        } else {
                                                            session_description =
                                                                session.get("description").asString
                                                        }

                                                        if (session.get("price").isJsonNull) {
                                                            session_price = ""
                                                        } else {
                                                            session_price =
                                                                session.get("price").asString
                                                        }


                                                        if (session.get("duration").isJsonNull) {
                                                            session_duration = ""
                                                        } else {
                                                            session_duration =
                                                                session.get("duration").asString
                                                        }

                                                        if (session.get("created_at").isJsonNull) {
                                                            session_created_at = ""
                                                        } else {
                                                            session_created_at =
                                                                session.get("created_at").asString
                                                        }

                                                        if (session.get("updated_at").isJsonNull) {
                                                            session_updated_at = ""
                                                        } else {
                                                            session_updated_at =
                                                                session.get("updated_at").asString
                                                        }

                                                        if (session.get("deleted_at").isJsonNull) {
                                                            session_deleted_at = ""
                                                        } else {
                                                            session_deleted_at =
                                                                session.get("deleted_at").asString
                                                        }

                                                        val sessionModel = SessionModel(
                                                            session_id,
                                                            session_location_id,
                                                            session_types,
                                                            session_title,
                                                            session_description,
                                                            session_price,
                                                            session_duration,
                                                            session_created_at,
                                                            session_updated_at,
                                                            session_deleted_at
                                                        )
                                                        sessionList!!.add(sessionModel)
                                                        sessionAdapter =
                                                            SessionAdapter(activity as MainActivity,
                                                                map,
                                                                sessionList!!,
                                                                { selected_it = it },
                                                                { selected_duration = it }, {
                                                                    selected_price = it
                                                                })


                                                        recycler_session_data.adapter =
                                                            sessionAdapter
                                                        recycler_session_data.layoutManager =
                                                            LinearLayoutManager(context)

                                                        sessionAdapter.notifyDataSetChanged()
                                                    }

                                                }
                                            }
                                            if (response.code() == 401) {

                                                CustomMethods().logOutUser(context!!)

                                            }

                                        } catch (e: Exception) {

                                        }
                                    }

                                })

                            }


                            if (type.equals("certificate")) {

                                txt_session_duration.text = "2 hours"
                                val session = data.get("session").asJsonObject
                                sessionList = ArrayList()


                                if (session.get("id").isJsonNull) {
                                    session_id = ""
                                } else {
                                    session_id = session.get("id").asString
                                }

                                if (session.get("location_id").isJsonNull) {
                                    session_location_id = ""
                                } else {
                                    session_location_id = session.get("location_id").asString
                                }

                                if (session.get("type").isJsonNull) {
                                    session_types = ""
                                } else {
                                    session_types = session.get("type").asString
                                }

                                if (session.get("title").isJsonNull) {
                                    session_title = ""
                                } else {
                                    session_title = session.get("title").asString
                                }

                                if (session.get("description").isJsonNull) {
                                    session_description = ""
                                } else {
                                    session_description = session.get("description").asString
                                }

                                if (session.get("price").isJsonNull) {
                                    session_price = ""
                                } else {
                                    session_price = session.get("price").asString
                                }


                                if (session.get("duration").isJsonNull) {
                                    session_duration = ""
                                } else {
                                    session_duration = session.get("duration").asString
                                }

                                if (session.get("created_at").isJsonNull) {
                                    session_created_at = ""
                                } else {
                                    session_created_at = session.get("created_at").asString
                                }

                                if (session.get("updated_at").isJsonNull) {
                                    session_updated_at = ""
                                } else {
                                    session_updated_at = session.get("updated_at").asString
                                }

                                if (session.get("deleted_at").isJsonNull) {
                                    session_deleted_at = ""
                                } else {
                                    session_deleted_at = session.get("deleted_at").asString
                                }

                                val sessionModel = SessionModel(
                                    session_id,
                                    session_location_id,
                                    session_types,
                                    session_title,
                                    session_description,
                                    session_price,
                                    session_duration,
                                    session_created_at,
                                    session_updated_at,
                                    session_deleted_at
                                )
                                sessionList!!.add(sessionModel)
                                sessionAdapter = SessionAdapter(activity as MainActivity,
                                    map,
                                    sessionList!!,
                                    { selected_it = it },
                                    { selected_duration = it }, {
                                        selected_price = it
                                    })


                                recycler_session_data.adapter = sessionAdapter
                                recycler_session_data.layoutManager =
                                    LinearLayoutManager(context)

                                sessionAdapter.notifyDataSetChanged()


                                userCheckBooking()
                            }
                            displayDate(calendar)

                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }

                } catch (e: Exception) {

                    Log.e("error", "" + e.toString())

                }

            }
        })
    }

    private fun userCheckBooking() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.checkBooking(
            "Bearer " + Preference().getUserData(context!!)?.token
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                Log.e("responseBooking", "" + response.body())
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            var data = response.body()!!.get("data").asJsonObject
                            var allowed = data.get("allowed").asString
                            if (allowed.equals("false")) {

                                val builder = AlertDialog.Builder(context)
                                //set title for alert dialog
                                builder.setTitle(R.string.app_name)
                                //set message for alert dialog
                                builder.setMessage("For Normal Sessions Booking User Need to Purchase Membership Subscription.Do you want to continue?")

                                //performing positive action
                                builder.setPositiveButton("Yes") { dialogInterface, which ->
                                    dialogInterface.dismiss()
                                    CustomMethods().openPage(
                                        activity!!,
                                        MainActivity().normalSession
                                    )
                                    activity!!.back.visibility = View.GONE
                                    activity!!.menu.visibility = View.VISIBLE
                                    (activity as MainActivity).clearSelection()
                                }

                                //performing negative action
                                builder.setNegativeButton("No") { dialogInterface, which ->
                                    dialogInterface.dismiss()
                                    CustomMethods().openPage(
                                        activity!!,
                                        MainActivity().dashboard
                                    )
                                    activity!!.back.visibility = View.GONE
                                    activity!!.menu.visibility = View.VISIBLE
                                    (activity as MainActivity).clearSelection()
                                }
                                // Create the AlertDialog
                                val alertDialog: AlertDialog = builder.create()
                                // Set other dialog properties
                                alertDialog.setCancelable(false)
                                alertDialog.show()


                            }

                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }

                } catch (e: Exception) {

                }
            }

        })
    }


    private fun checkDigit(number: Int): String {
        return if (number <= 9) "0$number" else number.toString()

    }


    @SuppressLint("InflateParams")
    private fun init() {
        model = (arguments!!.getSerializable("model") as MapData?)!!

        activity!!.menu.visibility = View.GONE
        activity!!.bottom_navigation.visibility = View.VISIBLE
        activity!!.back.visibility = View.VISIBLE
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)


        lat = Preference().getNewLat(context!!)
        long = Preference().getNewLong(context!!)
        range = Preference().getRange(context!!)

        var title_session = view!!.findViewById<TextView>(R.id.title_session)
        if (model!!.isRechedule) {
            title_session.text = "RESCHEDULE SESSION"
        }
        tvDescriptionLocationInner = view!!.findViewById(R.id.tv_description_loctaion_inner)
        tvNoOfInst = view!!.findViewById(R.id.tv_no_of_inst)
        tvAddToFavourite = view!!.findViewById(R.id.tv_add_to_favourite)
        imgLike = view!!.findViewById(R.id.img_like)
        linear_addToFav = view!!.findViewById(R.id.linear_addToFav)
        tvTotalNoOfFoils = view!!.findViewById(R.id.tv_total_no_of_foils)
        img_location_inner_bg = view!!.findViewById(R.id.img_location_inner_bg)
        txt_progression_type = view!!.findViewById(R.id.txt_progression_type)
        txt_session_duration = view!!.findViewById(R.id.txt_session_duration)
        webViewDesc = view!!.findViewById(R.id.webViewDesc)
//        txtSessionTitle = view!!.findViewById(R.id.txtSessionTitle)
        txtSessionPrice = view!!.findViewById(R.id.txt_session_price)


        val title: String = model!!.placeName
        tvScreenTitle.text = title

        val instructors: String = model!!.instructor_count
        tvNoOfInst.text = instructors

        telephone = model!!.telephone
        locationId = model!!.placeId

//        if (favoriteLocation == "0") {
//
//            tvAddToFavourite.text = resources.getString(R.string.add_to_favourite)
//            imgLike.background = ContextCompat.getDrawable(context!!, R.drawable.unlike)
//
//        } else {
//            tvAddToFavourite.text = resources.getString(R.string.added_as_favourite)
//            imgLike.background = ContextCompat.getDrawable(context!!, R.drawable.like)
//
//        }


        val foils: String = model!!.foils
        tvTotalNoOfFoils.text = foils

        val desc: String = model!!.placeDesc
        tvDescriptionLocationInner.text = desc

        var image: String = model!!.image

        Picasso.get().load(image).placeholder(R.drawable.imglocation).into(img_location_inner_bg)

        str = model!!.placeId
        paymentData = ArrayList()
        dayWiseSessionList = ArrayList()

        //Show popup when user comes 1st time for booking

        builder = Dialog(context!!)
        buildernew = Dialog(context!!)
        builderTimeSlot = Dialog(context!!)
        val layoutInflater = LayoutInflater.from(context!!)
        dialog = layoutInflater.inflate(R.layout.customer_waiver_popup, null)
        dialognew = layoutInflater.inflate(R.layout.customer_waiver_popup_new, null)

        tvDescr = dialog.findViewById(R.id.tv_descr)
        tv_descr_new = dialognew.findViewById(R.id.tv_descr_new)
        edt_your_name = dialognew.findViewById(R.id.edt_your_name)
        edt_your_last_name = dialognew.findViewById(R.id.edt_your_last_name)
        edt_minor_last_name = dialognew.findViewById(R.id.edt_minor_last_name)
        edt_minor_name = dialognew.findViewById(R.id.edt_minor_name)
        webView = dialognew.findViewById(R.id.webView)
        btnApprove = dialog.findViewById(R.id.btn_approve)
        btn_approve_new = dialognew.findViewById(R.id.btn_approve_new)
        spinnerGender = dialog.findViewById(R.id.spinner_gender)
        etAge = dialog.findViewById(R.id.et_age)
        txt_desc = dialog.findViewById(R.id.txt_desc)
        etWeight = dialog.findViewById(R.id.et_weight)
        etExperiencedesc = dialog.findViewById(R.id.et_experienceDesc)
        radioGroup = dialog.findViewById(R.id.radioGroup)
        radioNo = dialog.findViewById(R.id.radio_no)
        radioYes = dialog.findViewById(R.id.radio_yes)
        linearExpdesc = dialog.findViewById(R.id.linear_expDesc)

//
//        if (Preference().getUserData(context!!)!!.discover_lesson_status == "1" ||
//            Preference().getUserData(context!!)!!.certificate_status == "1"
//        ) {
//            builder.setCancelable(false)
//            builder.setContentView(dialog)
//            builder.show()
//        }

//        if (Preference().getUserData(context!!)!!.wavier_accepted.equals("")) {
//            builder.setCancelable(false)
//            builder.setContentView(dialog)
//            builder.show()
//        } else {
//            setDescFromApi()
//        }


        radioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.radio_yes -> {
                    val value: String = radioYes.text.toString()
                    if (value == "yes") {
                        expericenceDescription = value
                        linearExpdesc.visibility = View.VISIBLE
                    } else {
                        expericenceDescription = ""
                        linearExpdesc.visibility = View.GONE
                    }
                    selectedRadioButtonValue = value
                }
                R.id.radio_no -> {
                    val value: String = radioNo.text.toString()
                    if (value == "yes") {
                        expericenceDescription = value
                        linearExpdesc.visibility = View.VISIBLE
                    } else {
                        expericenceDescription = ""
                        linearExpdesc.visibility = View.GONE
                    }
                    selectedRadioButtonValue = value

                }

            }
        }
        spinnerGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }
            }

        txtMonth = view!!.findViewById(R.id.txt_month)
        txt11 = view!!.findViewById(R.id.txt11)
        txt12 = view!!.findViewById(R.id.txt12)
        txt13 = view!!.findViewById(R.id.txt13)
        txt14 = view!!.findViewById(R.id.txt14)
        txt15 = view!!.findViewById(R.id.txt15)
        txt16 = view!!.findViewById(R.id.txt16)
        txt17 = view!!.findViewById(R.id.txt17)
        txt21 = view!!.findViewById(R.id.txt21)
        txt22 = view!!.findViewById(R.id.txt22)
        txt23 = view!!.findViewById(R.id.txt23)
        txt24 = view!!.findViewById(R.id.txt24)
        txt25 = view!!.findViewById(R.id.txt25)
        txt26 = view!!.findViewById(R.id.txt26)
        txt27 = view!!.findViewById(R.id.txt27)
        txt31 = view!!.findViewById(R.id.txt31)
        txt32 = view!!.findViewById(R.id.txt32)
        txt33 = view!!.findViewById(R.id.txt33)
        txt34 = view!!.findViewById(R.id.txt34)
        txt35 = view!!.findViewById(R.id.txt35)
        txt36 = view!!.findViewById(R.id.txt36)
        txt37 = view!!.findViewById(R.id.txt37)
        txt41 = view!!.findViewById(R.id.txt41)
        txt42 = view!!.findViewById(R.id.txt42)
        txt43 = view!!.findViewById(R.id.txt43)
        txt44 = view!!.findViewById(R.id.txt44)
        txt45 = view!!.findViewById(R.id.txt45)
        txt46 = view!!.findViewById(R.id.txt46)
        txt47 = view!!.findViewById(R.id.txt47)
        txt51 = view!!.findViewById(R.id.txt51)
        txt52 = view!!.findViewById(R.id.txt52)
        txt53 = view!!.findViewById(R.id.txt53)
        txt54 = view!!.findViewById(R.id.txt54)
        txt55 = view!!.findViewById(R.id.txt55)
        txt56 = view!!.findViewById(R.id.txt56)
        txt57 = view!!.findViewById(R.id.txt57)
        txt61 = view!!.findViewById(R.id.txt61)
        txt62 = view!!.findViewById(R.id.txt62)
        txt63 = view!!.findViewById(R.id.txt63)
        txt64 = view!!.findViewById(R.id.txt64)
        txt65 = view!!.findViewById(R.id.txt65)
        txt66 = view!!.findViewById(R.id.txt66)
        txt67 = view!!.findViewById(R.id.txt67)

        lin11 = view!!.findViewById(R.id.lin11)
        lin12 = view!!.findViewById(R.id.lin12)
        lin13 = view!!.findViewById(R.id.lin13)
        lin14 = view!!.findViewById(R.id.lin14)
        lin15 = view!!.findViewById(R.id.lin15)
        lin16 = view!!.findViewById(R.id.lin16)
        lin17 = view!!.findViewById(R.id.lin17)
        lin21 = view!!.findViewById(R.id.lin21)
        lin22 = view!!.findViewById(R.id.lin22)
        lin23 = view!!.findViewById(R.id.lin23)
        lin24 = view!!.findViewById(R.id.lin24)
        lin25 = view!!.findViewById(R.id.lin25)
        lin26 = view!!.findViewById(R.id.lin26)
        lin27 = view!!.findViewById(R.id.lin27)
        lin31 = view!!.findViewById(R.id.lin31)
        lin32 = view!!.findViewById(R.id.lin32)
        lin33 = view!!.findViewById(R.id.lin33)
        lin34 = view!!.findViewById(R.id.lin34)
        lin35 = view!!.findViewById(R.id.lin35)
        lin36 = view!!.findViewById(R.id.lin36)
        lin37 = view!!.findViewById(R.id.lin37)
        lin41 = view!!.findViewById(R.id.lin41)
        lin42 = view!!.findViewById(R.id.lin42)
        lin43 = view!!.findViewById(R.id.lin43)
        lin44 = view!!.findViewById(R.id.lin44)
        lin45 = view!!.findViewById(R.id.lin45)
        lin46 = view!!.findViewById(R.id.lin46)
        lin47 = view!!.findViewById(R.id.lin47)
        lin51 = view!!.findViewById(R.id.lin51)
        lin52 = view!!.findViewById(R.id.lin52)
        lin53 = view!!.findViewById(R.id.lin53)
        lin54 = view!!.findViewById(R.id.lin54)
        lin55 = view!!.findViewById(R.id.lin55)
        lin56 = view!!.findViewById(R.id.lin56)
        lin57 = view!!.findViewById(R.id.lin57)
        lin61 = view!!.findViewById(R.id.lin61)
        lin62 = view!!.findViewById(R.id.lin62)
        lin63 = view!!.findViewById(R.id.lin63)
        lin64 = view!!.findViewById(R.id.lin64)
        lin65 = view!!.findViewById(R.id.lin65)
        lin66 = view!!.findViewById(R.id.lin66)
        lin67 = view!!.findViewById(R.id.lin67)

        recycler_session_data = view!!.findViewById(R.id.recycler_session_data)


        builderTimeSlot = Dialog(context!!)
        val layoutInflater1 = LayoutInflater.from(context)
        dialogAvailableSession =
            layoutInflater1.inflate(
                R.layout.available_session_popup,
                null
            )

        tvSelectedDate =
            dialogAvailableSession.findViewById(R.id.tv_selected_date)

        txt_available_from_time =
            dialogAvailableSession.findViewById(R.id.txt_available_from_time)

        txt_available_to_time =
            dialogAvailableSession.findViewById(R.id.txt_available_to_time)

        txt_start_time =
            dialogAvailableSession.findViewById(R.id.txt_start_time)

        txt_end_time =
            dialogAvailableSession.findViewById(R.id.txt_end_time)

        lin_selected_time =
            dialogAvailableSession.findViewById(R.id.lin_selected_time)

        spinner_timing =
            dialogAvailableSession.findViewById(R.id.spinner_timing)

        tv_selected_date =
            dialogAvailableSession.findViewById(R.id.tv_selected_date)

        btn_session_popup_submit =
            dialogAvailableSession.findViewById(R.id.btn_session_popup_submit)

        lin_instructor_board =
            dialogAvailableSession.findViewById(R.id.lin_instructor_board)

        lin_instructor =
            dialogAvailableSession.findViewById(R.id.lin_instructor)

        spinner_instructor =
            dialogAvailableSession.findViewById(R.id.spinner_instructor)

        spinner_board =
            dialogAvailableSession.findViewById(R.id.spinner_board)

        btn_session_continue =
            dialogAvailableSession.findViewById(R.id.btn_session_continue)



        rvAvailabletimeslots =
            dialogAvailableSession.findViewById(R.id.rv_availableTimeSlots)
        tvNoDataAvailable = dialogAvailableSession.findViewById(R.id.tvNoDataAvailable)

        if (builder.isShowing) {
            apicall()
        }


    }

    private fun apicall() {
        val api: Api = Connection().getCon(context!!)
        api.wavier_signoff(
            "Bearer " + Preference().getUserData(context!!)?.token
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        var data = response.body()!!.get("data").asJsonObject
                        var value = data.get("value").asString

                        txt_desc.text = value

                    }
                }
                if (response.code() == 401) {

                    CustomMethods().logOutUser(context!!)

                }
            }
        })
    }

    private fun displayDate(calendar: Calendar) {


        cYear = calendar.get(Calendar.YEAR).toString()
        txt_year.text = cYear

        monthNumber =
            checkDigit(Integer.valueOf((calendar.get(Calendar.MONTH) + 1).toString()))
        monthdate = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthdate.format(calendar.time)
        txtMonth.text = monthName

        val day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        var firstDay = 2
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> {
                firstDay = 2
            }
            Calendar.MONDAY -> {
                firstDay = 8
            }
            Calendar.TUESDAY -> {
                firstDay = 7
            }
            Calendar.WEDNESDAY -> {
                firstDay = 6
            }
            Calendar.THURSDAY -> {
                firstDay = 5
            }
            Calendar.FRIDAY -> {
                firstDay = 4
            }
            Calendar.SATURDAY -> {
                firstDay = 3
            }
        }
        var isFirstWeek = true
        var column = 1
        var j = 0
        clearDate()


        for (i in 1..day) {


            if (i % firstDay == 0 && isFirstWeek) {

                column++
                isFirstWeek = false
                j = 0

            } else {

                if (j % 7 == 0 && !isFirstWeek) {
                    column++
                }

            }
            setDate(i.toString(), column.toString(), calendar)
            j++
        }

    }

    private fun setDate(
        day: String,
        column: String,
        calendar: Calendar
    ) {

        monthdate = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthdate.format(calendar.time)
        txtMonth.text = monthName


        if (column == "5") {
            lin_sixth.visibility = View.GONE
        } else {
            lin_sixth.visibility = View.VISIBLE
        }
        if (column == "6") {
            lin_sixth.visibility = View.VISIBLE
        }
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day))

        when (calendar.get(Calendar.DAY_OF_WEEK)) {

            Calendar.SUNDAY -> {
                when (column) {
                    "1" -> {
                        txt17.text = day
                        getTextFn(lin17, txt17, Calendar.SUNDAY)
                    }
                    "2" -> {
                        txt27.text = day
                        getTextFn(lin27, txt27, Calendar.SUNDAY)
                    }
                    "3" -> {
                        txt37.text = day
                        getTextFn(lin37, txt37, Calendar.SUNDAY)
                    }
                    "4" -> {
                        txt47.text = day
                        getTextFn(lin47, txt47, Calendar.SUNDAY)
                    }
                    "5" -> {
                        txt57.text = day
                        getTextFn(lin57, txt57, Calendar.SUNDAY)
                    }
                    "6" -> {
                        txt67.text = day
                        getTextFn(lin67, txt67, Calendar.SUNDAY)
                    }
                }
                return
            }


            Calendar.MONDAY -> {

                when (column) {
                    "1" -> {
                        txt11.text = day
                        getTextFn(lin11, txt11, Calendar.MONDAY)
                    }
                    "2" -> {
                        txt21.text = day
                        getTextFn(lin21, txt21, Calendar.MONDAY)
                    }
                    "3" -> {
                        txt31.text = day
                        getTextFn(lin31, txt31, Calendar.MONDAY)
                    }
                    "4" -> {
                        txt41.text = day
                        getTextFn(lin41, txt41, Calendar.MONDAY)
                    }
                    "5" -> {
                        txt51.text = day
                        getTextFn(lin51, txt51, Calendar.MONDAY)
                    }
                    "6" -> {
                        txt61.text = day
                        getTextFn(lin61, txt61, Calendar.MONDAY)
                    }
                }
                return
            }


            Calendar.TUESDAY -> {

                when (column) {
                    "1" -> {
                        txt12.text = day
                        getTextFn(lin12, txt12, Calendar.TUESDAY)
                    }
                    "2" -> {
                        txt22.text = day
                        getTextFn(lin22, txt22, Calendar.TUESDAY)
                    }
                    "3" -> {
                        txt32.text = day
                        getTextFn(lin32, txt32, Calendar.TUESDAY)
                    }
                    "4" -> {
                        txt42.text = day
                        getTextFn(lin42, txt42, Calendar.TUESDAY)
                    }
                    "5" -> {
                        txt52.text = day
                        getTextFn(lin52, txt52, Calendar.TUESDAY)
                    }
                    "6" -> {
                        txt62.text = day
                        getTextFn(lin62, txt62, Calendar.TUESDAY)
                    }
                }
                return
            }


            Calendar.WEDNESDAY -> {

                when (column) {
                    "1" -> {
                        txt13.text = day
                        getTextFn(lin13, txt13, Calendar.WEDNESDAY)
                    }
                    "2" -> {
                        txt23.text = day
                        getTextFn(lin23, txt23, Calendar.WEDNESDAY)
                    }
                    "3" -> {
                        txt33.text = day
                        getTextFn(lin33, txt33, Calendar.WEDNESDAY)
                    }
                    "4" -> {
                        txt43.text = day
                        getTextFn(lin43, txt43, Calendar.WEDNESDAY)
                    }
                    "5" -> {
                        txt53.text = day
                        getTextFn(lin53, txt53, Calendar.WEDNESDAY)
                    }
                    "6" -> {
                        txt63.text = day
                        getTextFn(lin63, txt63, Calendar.WEDNESDAY)
                    }
                }
                return
            }


            Calendar.THURSDAY -> {

                when (column) {
                    "1" -> {
                        txt14.text = day
                        getTextFn(lin14, txt14, Calendar.THURSDAY)
                    }
                    "2" -> {
                        txt24.text = day
                        getTextFn(lin24, txt24, Calendar.THURSDAY)
                    }
                    "3" -> {
                        txt34.text = day
                        getTextFn(lin34, txt34, Calendar.THURSDAY)
                    }
                    "4" -> {
                        txt44.text = day
                        getTextFn(lin44, txt44, Calendar.THURSDAY)
                    }
                    "5" -> {
                        txt54.text = day
                        getTextFn(lin54, txt54, Calendar.THURSDAY)
                    }
                    "6" -> {
                        txt64.text = day
                        getTextFn(lin64, txt64, Calendar.THURSDAY)
                    }
                }
                return
            }

            Calendar.FRIDAY -> {

                when (column) {
                    "1" -> {
                        txt15.text = day
                        getTextFn(lin15, txt15, Calendar.FRIDAY)
                    }
                    "2" -> {
                        txt25.text = day
                        getTextFn(lin25, txt25, Calendar.FRIDAY)
                    }
                    "3" -> {
                        txt35.text = day
                        getTextFn(lin35, txt35, Calendar.FRIDAY)
                    }
                    "4" -> {
                        txt45.text = day
                        getTextFn(lin45, txt45, Calendar.FRIDAY)
                    }
                    "5" -> {
                        txt55.text = day
                        getTextFn(lin55, txt55, Calendar.FRIDAY)
                    }
                    "6" -> {
                        txt65.text = day
                        getTextFn(lin65, txt65, Calendar.FRIDAY)
                    }
                }
                return
            }

            Calendar.SATURDAY ->

                when (column) {
                    "1" -> {
                        txt16.text = day
                        getTextFn(lin16, txt16, Calendar.SATURDAY)
                    }
                    "2" -> {
                        txt26.text = day
                        getTextFn(lin26, txt26, Calendar.SATURDAY)
                    }
                    "3" -> {
                        txt36.text = day
                        getTextFn(lin36, txt36, Calendar.SATURDAY)
                    }
                    "4" -> {
                        txt46.text = day
                        getTextFn(lin46, txt46, Calendar.SATURDAY)
                    }
                    "5" -> {
                        txt56.text = day
                        getTextFn(lin56, txt56, Calendar.SATURDAY)
                    }
                    "6" -> {
                        txt66.text = day
                        getTextFn(lin66, txt66, Calendar.SATURDAY)
                    }
                }

        }

    }

    private fun getTextFn(
        lin11: LinearLayout,
        txt11: TextView,
        dayNo: Int
    ) {
        clearLinear()

//        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)


        //unavailable dates for booking or holiday dates
        api.locationHolidays(
            "Bearer " + Preference().getUserData(context!!)?.token,
            locationId, monthNumber, cYear
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                CustomLoader().stopLoader(dialog)
            }

            @SuppressLint("InflateParams")
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                CustomLoader().stopLoader(dialog)
                Log.e("responseHoli", "" + response.body())
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        try {


                            dateArray = response.body()!!.get("data").asJsonArray
                            if (dateArray.size() > 0) {
                                val strArrayList: ArrayList<String> = ArrayList()
                                var monthName = ""
                                strArrayList.clear()
                                for (i in 0 until dateArray.size()) {
                                    val splitDate = dateArray.get(i).asString.split("/")
                                    val hMonth = splitDate[0]
                                    val hDay = splitDate[1]
                                    val hYear = splitDate[2]
                                    d = "$hYear-$hMonth-$hDay"
                                    strArrayList.add(d)
                                }
                                for (j in 0 until strArrayList.size) {
                                    val dates: ArrayList<String> =
                                        strArrayList[j].split("-") as ArrayList<String>
                                    val year = dates[0]
                                    val month = dates[1]
                                    val day = dates[2]
                                    when (month) {
                                        "01" -> monthName = "January"
                                        "02" -> monthName = "February"
                                        "03" -> monthName = "March"
                                        "04" -> monthName = "April"
                                        "05" -> monthName = "May"
                                        "06" -> monthName = "June"
                                        "07" -> monthName = "July"
                                        "08" -> monthName = "August"
                                        "09" -> monthName = "September"
                                        "10" -> monthName = "October"
                                        "11" -> monthName = "November"
                                        "12" -> monthName = "December"
                                    }
                                    val dayInNumber = checkDigit((txt11.text.toString()).toInt())

                                    lin11.setBackgroundColor(
                                        ContextCompat.getColor(
                                            context!!,
                                            android.R.color.transparent
                                        )
                                    )

                                    if ((txtMonth.text.toString() == monthName) && (dayInNumber == day) && (cYear == year)) {


                                        lin11.setBackgroundColor(
                                            ContextCompat.getColor(
                                                context!!,
                                                android.R.color.transparent
                                            )
                                        )
                                        txt11.setTextColor(
                                            ContextCompat.getColorStateList(
                                                context!!,
                                                R.color.black
                                            )
                                        )
                                        lin11.setOnClickListener {
                                            //                                            Toast.makeText(
//                                                context,
//                                                "Please Long Click on Date",
//                                                Toast.LENGTH_LONG
//                                            ).show()
                                            Log.e("linkAffiliate", "linkAffiliate")
                                        }

                                        lin11.setOnLongClickListener {
                                            var monthN = ""
                                            when (txtMonth.text.toString()) {
                                                "January" -> monthN = "01"
                                                "February" -> monthN = "02"
                                                "March" -> monthN = "03"
                                                "April" -> monthN = "04"
                                                "May" -> monthN = "05"
                                                "June" -> monthN = "06"
                                                "July" -> monthN = "07"
                                                "August" -> monthN = "08"
                                                "September" -> monthN = "09"
                                                "October" -> monthN = "10"
                                                "November" -> monthN = "11"
                                                "December" -> monthN = "12"
                                            }
//                                        val date =
//                                            monthN + "-" + txt11.text.toString() + "-" + cYear
//                                        val sessionDate =
//                                            monthN + "/" + txt11.text.toString() + "/" + cYear
//
//                                        val inFormat =
//                                            SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
//                                        val date1 = inFormat.parse(date)
//
//                                        Log.e("informat", "" + inFormat)
//
//                                        val outFormat =
//                                            SimpleDateFormat("EEEE", Locale.ENGLISH)
//                                        dayName = outFormat.format(date1!!)
//                                        tvSelectedDate.text = date

                                            selected_dates =
                                                monthN + "-" + txt11.text.toString() + "-" + cYear
                                            sessionDate =
                                                monthN + "/" + txt11.text.toString() + "/" + cYear

                                            val inFormat =
                                                SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
                                            val date1 = inFormat.parse(selected_dates)
                                            val outFormat =
                                                SimpleDateFormat("EEEE", Locale.ENGLISH)
                                            dayName = outFormat.format(date1!!)
                                            tvSelectedDate.text = selected_dates

                                            var c: Date = Calendar.getInstance().time

                                            var df = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
                                            var current_date: String = df.format(c)


                                            var formate_of_date = SimpleDateFormat("MM-dd-yyyy")
                                            var c_date_formate = formate_of_date.parse(current_date)

                                            var selected_date_formate =
                                                formate_of_date.parse(selected_dates)


                                            //get time slots
                                            if (!selected_it.equals("")) {

                                                if (c_date_formate.compareTo(selected_date_formate) <= 0) {
                                                    getNewTimeSlot(locationId, dayName)

//                                        getTimeSlot(dayName, sessionDate)

//                                            builderTimeSlot.setContentView(dialogAvailableSession)
//                                            builderTimeSlot.show()
//                                            builderTimeSlot.setCancelable(false)
//
//                                            val imgClosepopup =
//                                                dialogAvailableSession.findViewById<ImageView>(R.id.img_closePopup)
//
//                                            imgClosepopup.setOnClickListener {
//                                                txt_end_time.text = ""
//                                                txt_start_time.text = ""
//                                                builderTimeSlot.dismiss()
//                                            }
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Please Select the date which is greater than the current date",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Please select one session from above",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                            true
                                        }
                                        break
                                    } else {

                                        lin11.setBackgroundColor(
                                            ContextCompat.getColor(
                                                context!!,
                                                R.color.colorPrimaryDark
                                            )
                                        )
                                        txt11.setTextColor(
                                            ContextCompat.getColorStateList(
                                                context!!,
                                                R.color.white
                                            )
                                        )
                                        lin11.setOnClickListener {
                                            builderTimeSlot.dismiss()
                                            Toast.makeText(
                                                context,
                                                "Date Not Available",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
//                                        lin11.setBackgroundColor(
//                                            ContextCompat.getColor(
//                                                context!!,
//                                                android.R.color.transparent
//                                            )
//                                        )
//                                        txt11.setTextColor(
//                                            ContextCompat.getColorStateList(
//                                                context!!,
//                                                R.color.black
//                                            )
//                                        )
//
//                                        lin11.setOnLongClickListener {
//                                            var monthN = ""
//                                            when (txtMonth.text.toString()) {
//                                                "January" -> monthN = "01"
//                                                "February" -> monthN = "02"
//                                                "March" -> monthN = "03"
//                                                "April" -> monthN = "04"
//                                                "May" -> monthN = "05"
//                                                "June" -> monthN = "06"
//                                                "July" -> monthN = "07"
//                                                "August" -> monthN = "08"
//                                                "September" -> monthN = "09"
//                                                "October" -> monthN = "10"
//                                                "November" -> monthN = "11"
//                                                "December" -> monthN = "12"
//                                            }
////                                        val date =
////                                            monthN + "-" + txt11.text.toString() + "-" + cYear
////                                        val sessionDate =
////                                            monthN + "/" + txt11.text.toString() + "/" + cYear
////
////                                        val inFormat =
////                                            SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
////                                        val date1 = inFormat.parse(date)
////
////                                        Log.e("informat", "" + inFormat)
////
////                                        val outFormat =
////                                            SimpleDateFormat("EEEE", Locale.ENGLISH)
////                                        dayName = outFormat.format(date1!!)
////                                        tvSelectedDate.text = date
//
//                                            selected_dates =
//                                                monthN + "-" + txt11.text.toString() + "-" + cYear
//                                            sessionDate =
//                                                monthN + "/" + txt11.text.toString() + "/" + cYear
//
//                                            val inFormat =
//                                                SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
//                                            val date1 = inFormat.parse(selected_dates)
//                                            val outFormat =
//                                                SimpleDateFormat("EEEE", Locale.ENGLISH)
//                                            dayName = outFormat.format(date1!!)
//                                            tvSelectedDate.text = selected_dates
//
//                                            var c: Date = Calendar.getInstance().time
//
//                                            var df = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
//                                            var current_date: String = df.format(c)
//
//
//                                            var formate_of_date = SimpleDateFormat("MM-dd-yyyy")
//                                            var c_date_formate = formate_of_date.parse(current_date)
//
//                                            var selected_date_formate =
//                                                formate_of_date.parse(selected_dates)
//
//
//                                            //get time slots
//                                            if (!selected_it.equals("")) {
//
//                                                if (c_date_formate.compareTo(selected_date_formate) <= 0) {
//                                                    getNewTimeSlot(locationId, dayName)
//
////                                        getTimeSlot(dayName, sessionDate)
//
////                                            builderTimeSlot.setContentView(dialogAvailableSession)
////                                            builderTimeSlot.show()
////                                            builderTimeSlot.setCancelable(false)
////
////                                            val imgClosepopup =
////                                                dialogAvailableSession.findViewById<ImageView>(R.id.img_closePopup)
////
////                                            imgClosepopup.setOnClickListener {
////                                                txt_end_time.text = ""
////                                                txt_start_time.text = ""
////                                                builderTimeSlot.dismiss()
////                                            }
//                                                } else {
//                                                    Toast.makeText(
//                                                        context,
//                                                        "Please Select the date which is greater than the current date",
//                                                        Toast.LENGTH_LONG
//                                                    ).show()
//                                                }
//                                            } else {
//                                                Toast.makeText(
//                                                    context,
//                                                    "Please Select the Above Section First",
//                                                    Toast.LENGTH_LONG
//                                                ).show()
//                                            }
//                                            true
//                                        }
                                    }
                                }
                            } else {

                                lin11.setBackgroundColor(
                                    ContextCompat.getColor(
                                        context!!,
                                        android.R.color.transparent
                                    )
                                )
                                txt11.setTextColor(
                                    ContextCompat.getColorStateList(
                                        context!!,
                                        R.color.black
                                    )
                                )


                                lin11.setOnLongClickListener {
                                    var monthN = ""
                                    when (txtMonth.text.toString()) {
                                        "January" -> monthN = "01"
                                        "February" -> monthN = "02"
                                        "March" -> monthN = "03"
                                        "April" -> monthN = "04"
                                        "May" -> monthN = "05"
                                        "June" -> monthN = "06"
                                        "July" -> monthN = "07"
                                        "August" -> monthN = "08"
                                        "September" -> monthN = "09"
                                        "October" -> monthN = "10"
                                        "November" -> monthN = "11"
                                        "December" -> monthN = "12"
                                    }
                                    val date =
                                        monthN + "-" + txt11.text.toString() + "-" + cYear
                                    sessionDate =
                                        monthN + "/" + txt11.text.toString() + "/" + cYear

                                    val inFormat =
                                        SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
                                    val date1 = inFormat.parse(date)
                                    val outFormat =
                                        SimpleDateFormat("EEEE", Locale.ENGLISH)
                                    dayName = outFormat.format(date1!!)
                                    tvSelectedDate.text = date

                                    var c: Date = Calendar.getInstance().time

                                    var df = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
                                    var current_date: String = df.format(c)


                                    var formate_of_date = SimpleDateFormat("MM-dd-yyyy")
                                    var c_date_formate = formate_of_date.parse(current_date)

                                    var selected_date_formate = formate_of_date.parse(date)

                                    //get time slots

                                    if (!selected_it.equals("")) {
                                        if (c_date_formate.compareTo(selected_date_formate) <= 0) {
                                            getNewTimeSlot(locationId, dayName)

//                                        getTimeSlot(dayName, sessionDate)

//                                        builderTimeSlot.setContentView(dialogAvailableSession)
//                                        builderTimeSlot.show()
//                                        builderTimeSlot.setCancelable(false)
//
//                                        val imgClosepopup =
//                                            dialogAvailableSession.findViewById<ImageView>(R.id.img_closePopup)
//
//                                        imgClosepopup.setOnClickListener {
//                                            txt_end_time.text = "Select"
//                                            txt_start_time.text = "Select"
//                                            builderTimeSlot.dismiss()
//                                        }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Please Select the date which is greater than the current date",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please Select the Above Section First",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    true
                                }
                            }

                        } catch (e: Exception) {

                            Log.e("error", "" + e)
                        }
                    }


                }
                if (response.code() == 401) {

                    CustomMethods().logOutUser(context!!)

                }
            }
        })
    }

    private fun clearLinear() {
        lin11.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt11.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin12.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt12.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin13.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt13.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin14.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt14.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin15.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt15.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin16.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt16.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin17.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt17.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin21.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt21.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin22.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt22.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin23.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt23.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin24.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt24.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin25.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt25.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin26.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt26.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin27.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt27.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin31.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt31.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin32.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt32.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin33.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt33.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin34.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt34.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin35.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt35.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin36.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt36.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin37.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt37.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin41.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt41.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin42.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt42.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin43.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt43.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin44.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt44.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin45.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt45.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin46.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt46.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin47.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt47.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin51.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt51.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin52.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt52.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin53.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt53.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin54.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt54.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin55.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt55.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin56.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt56.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin57.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt57.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin61.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt61.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin62.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt62.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin63.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt63.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin64.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt64.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin65.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt65.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin66.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt66.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )
        lin67.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt67.setTextColor(
            ContextCompat.getColorStateList(
                context!!,
                R.color.black
            )
        )

    }

    private fun getNewTimeSlot(locationId: String, dayName: String) {

        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        call = api.getNewTimeSlot(
            "Bearer " + Preference().getUserData(context!!)?.token,
            locationId,
            dayName
        )
        Log.e("locationId", "" + locationId)
        Log.e("dayName", "" + dayName)

        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                Log.e("NewTimeResponse", "" + response.body())
                try {


                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            var activeHoursArrayList: ArrayList<ActiveHours> = ArrayList()


                            var data = response.body()!!.get("data").asJsonObject
                            if (data.get("id").isJsonNull) {
                                activehours_id = ""
                            } else {
                                activehours_id = data.get("id").asString
                            }
                            if (data.get("location_id").isJsonNull) {
                                activehours_location_id = ""
                            } else {
                                activehours_location_id = data.get("location_id").asString
                            }
                            if (data.get("day").isJsonNull) {
                                activehours_day = ""
                            } else {
                                activehours_day = data.get("day").asString
                            }
                            if (data.get("start_time").isJsonNull) {
                                activehours_start_time = ""
                            } else {
                                activehours_start_time = data.get("start_time").asString
                            }
                            if (data.get("end_time").isJsonNull) {
                                activehours_end_time = ""
                            } else {
                                activehours_end_time = data.get("end_time").asString
                            }
                            if (data.get("available").isJsonNull) {
                                activehours_available = "0"
                            } else {
                                activehours_available = data.get("available").asString
                            }
                            if (data.get("created_at").isJsonNull) {
                                activehours_created_at = ""
                            } else {
                                activehours_created_at = data.get("created_at").asString
                            }
                            if (data.get("updated_at").isJsonNull) {
                                activehours_updated_at = ""
                            } else {
                                activehours_updated_at = data.get("updated_at").asString
                            }
                            if (data.get("deleted_at").isJsonNull) {
                                activehours_deleted_at = ""
                            } else {
                                activehours_deleted_at = data.get("deleted_at").asString
                            }
                            var activeHours = ActiveHours(
                                activehours_id,
                                activehours_location_id,
                                activehours_day,
                                activehours_start_time,
                                activehours_end_time,
                                activehours_available,
                                activehours_created_at,
                                activehours_updated_at,
                                activehours_deleted_at

                            )
                            activeHoursArrayList.add(activeHours)

//                        builderTimeSlot.setContentView(dialogAvailableSession)
//                        builderTimeSlot.show()
//                        builderTimeSlot.setCancelable(false)

                            var sdf = SimpleDateFormat("hh:mm:ss")
                            var sdfs = SimpleDateFormat("hh:mm a")

                            var s_time: Date = sdf.parse(activehours_start_time)
                            Log.e("s_time", "" + sdfs.format(s_time))

                            var e_time: Date = sdf.parse(activehours_end_time)
                            Log.e("s_time", "" + sdfs.format(e_time))



                            txt_available_from_time.text = sdfs.format(s_time).toString()
                            txt_available_to_time.text = sdfs.format(e_time).toString()

                            txt_start_time.setOnClickListener {
                                val cal = Calendar.getInstance()


                                var timePickerDialog =
                                    com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                                        this@LocationFinderInnerFragment,
                                        cal.get(Calendar.HOUR_OF_DAY),
                                        cal.get(Calendar.MINUTE),
                                        false
                                    )
                                timePickerDialog.isThemeDark = false
                                timePickerDialog.title = "TimePicker Title"
                                timePickerDialog.setTimeInterval(1, 30, 60)
                                timePickerDialog.accentColor = Color.parseColor("#477497")

                                timePickerDialog.setOnCancelListener {
                                    Log.d("TimePicker", "Dialog was cancelled")
                                }

                                fragmentManager?.let { it1 ->
                                    timePickerDialog.show(
                                        it1,
                                        "Timepickerdialog"
                                    )
                                }

                            }
                            if (!activehours_available.equals("0")) {

                                builderTimeSlot.setContentView(dialogAvailableSession)
                                builderTimeSlot.show()
                                builderTimeSlot.setCancelable(false)

                                val imgClosepopup =
                                    dialogAvailableSession.findViewById<ImageView>(R.id.img_closePopup)

                                imgClosepopup.setOnClickListener {
                                    txt_end_time.text = "Select"
                                    txt_start_time.text = "Select"
                                    txt_start_time.isEnabled = true
                                    lin_selected_time.visibility = View.GONE
                                    lin_instructor_board.visibility = View.GONE
                                    builderTimeSlot.dismiss()
                                }
                            }

                        }

                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }
                } catch (e: Exception) {
                    Log.e("error", "" + e.toString())
                }
            }
        })

    }

    private fun getTimeData(
        locationId: String,
        dayName: String,
        sessionDate: String,
        cDate: String,
        addedTime: String
    ) {
        val dialog12: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)

        Log.e("locationId", "" + locationId)
        Log.e("locationId", "" + locationId)
        Log.e("dayName", "" + dayName)
        Log.e("sessionDate", "" + sessionDate)
        Log.e("cDate", "" + cDate)
        Log.e("addedTime", "" + addedTime)

        api.getTimeDatas(
            "Bearer " + Preference().getUserData(context!!)?.token,
            locationId, dayName, sessionDate, cDate, addedTime
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog12.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog12.dismiss()
                Log.e("responseGetTime", "" + response.body())
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        if (Preference().getUserData(context!!)!!.wavier_accepted.equals("")) {
                            builder.setCancelable(false)
                            builder.setContentView(dialog)
                            builder.show()
                        } else {
                            setDescFromApi()
                        }

                        var data = response.body()!!.get("data").asJsonObject
                        var booking_status = data.get("booking").asString

                        if (booking_status.equals("false")) {

                            lin_instructor_board.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "" + data.get("message").asString,
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

                            lin_instructor_board.visibility = View.VISIBLE
                            jarray_instructor = data.get("instructors").asJsonArray
                            if (jarray_instructor!!.size() > 0) {
                                lin_instructor.visibility = View.VISIBLE
                                var instructor_string_array = ArrayList<String>()
                                for (i in 0 until jarray_instructor!!.size()) {

                                    var jobject_instructor = jarray_instructor!!.get(i).asJsonObject
                                    instructor_string_array.add(jobject_instructor.get("name").asString)

                                }

                                var arrayAdapter: ArrayAdapter<String> =
                                    ArrayAdapter<String>(
                                        context!!,
                                        android.R.layout.simple_spinner_item,
                                        instructor_string_array
                                    )
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinner_instructor.adapter = arrayAdapter


                                spinner_instructor.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onNothingSelected(parent: AdapterView<*>?) {

                                        }

                                        override fun onItemSelected(
                                            parent: AdapterView<*>?,
                                            view: View?,
                                            position: Int,
                                            id: Long
                                        ) {


                                        }

                                    }

                            } else {
                                lin_instructor.visibility = View.GONE
                            }
                            jarray_boards = data.get("boards").asJsonArray
                            if (jarray_boards!!.size() > 0) {

                                var boards_string_array = ArrayList<String>()
                                for (i in 0 until jarray_boards!!.size()) {

                                    var jobject_board = jarray_boards!!.get(i).asJsonObject
                                    boards_string_array.add(jobject_board.get("model").asString)
                                }
                                var arrayAdapter: ArrayAdapter<String> =
                                    ArrayAdapter<String>(
                                        context!!,
                                        android.R.layout.simple_spinner_item,
                                        boards_string_array
                                    )
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinner_board.adapter = arrayAdapter


                                spinner_board.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onNothingSelected(parent: AdapterView<*>?) {

                                        }

                                        override fun onItemSelected(
                                            parent: AdapterView<*>?,
                                            view: View?,
                                            position: Int,
                                            id: Long
                                        ) {

                                        }

                                    }

                            }

                            btn_session_continue.setOnClickListener {


                                if (jarray_instructor!!.size() > 0) {
                                    instructor_id =
                                        jarray_instructor!![spinner_instructor.selectedItemId.toInt()].asJsonObject.get(
                                            "id"
                                        ).asString
                                } else {
                                    instructor_id = ""
                                }


                                var board_id =
                                    jarray_boards!![spinner_board.selectedItemId.toInt()].asJsonObject.get(
                                        "id"
                                    ).asString


                                builderTimeSlot.dismiss()
                                if (type.equals("normal")) {
                                    checkUserBookingStatus(
                                        locationId,
                                        selected_price,
                                        start_time_24,
                                        end_time_24,
                                        type,
                                        sessionDate,
                                        null,
                                        board_id,
                                        selected_it
                                    )
                                } else {

                                    checkUserBookingStatus(
                                        locationId,
                                        selected_price,
                                        start_time_24,
                                        end_time_24,
                                        type,
                                        sessionDate,
                                        instructor_id,
                                        board_id,
                                        selected_it
                                    )
                                }

                            }

                        }

                    }
                }
                if (response.code() == 401) {

                    CustomMethods().logOutUser(context!!)

                }
            }
        })

    }


    private fun updateSession(
        locationId: String,
        selectedPrice: String,
        start_time: String,
        end_time: String,
        type: String,
        selected_date: String,
        selected_instructor: String?,
        selected_board: String,
        selectedIt: String
    ) {

        val dialog: Dialog = CustomLoader().loader(context)
        dialog.show()
        val api: Api = Connection().getCon(context!!)
        //check booking is allowed or not
//        call = api.checkBookingSession("Bearer " + Preference().getUserData(context!!)?.token)

//        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//
//                dialog.dismiss()
//            }

//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//
//                dialog.dismiss()
//                Log.e("user_booking_res", "" + response.body())
//
//
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//
//                        var data = response.body()!!.get("data").asJsonObject
//                        var allowed = data.get("allowed").asString
//
//                        if (allowed.equals("true")) {

        var data: JSONObject = JSONObject(map.response)

//        Log.e("auth_token", "" + "Bearer " + Preference().getUserData(context!!)?.token);
//        Log.e("selected_date", "" + selected_date);
//        Log.e("start_time", "" + start_time);
//        Log.e("end_time", "" + end_time);
//        Log.e("selected_board", "" + selected_board);
//        Log.e("selected_instructor!!", "" + selected_instructor!!);
//        Log.e("type", "" + type);
//        Log.e("map.placeId", "" + data.getJSONObject("data").getString("id"));
//        Log.e("response", "" + map.response);

        if (selected_instructor == null) {
            call =
                api.update_session(
                    "Bearer " + Preference().getUserData(context!!)?.token,
                    selected_date,
                    start_time,
                    end_time,
                    selected_board,
                    type,
                    data.getJSONObject("data").getString("id")
                )
        } else {
            call =
                api.update_session(
                    "Bearer " + Preference().getUserData(context!!)?.token,
                    selected_date,
                    start_time,
                    end_time,
                    selected_board,
                    selected_instructor,
                    type,
                    data.getJSONObject("data").getString("id")
                )
        }

        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("asd0", "" + t.message)
                Log.e("asd1", "" + call)
                dialog.dismiss()
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {

                dialog.dismiss()
                if (response.code() == 400) {
                    Toast.makeText(
                        context,
                        "Either you alreay passed or failed session or session is canceled.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                if (response.code() == 401) {
                    CustomMethods().logOutUser(context!!)
                }
                if (response.code() == 200) {
                    Toast.makeText(
                        context,
                        "Session successfully updated !",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    CustomMethods().openPage(
                        activity!!,
                        MainActivity().calendar
                    )
                }
            }
        })


//                        } else {
//                            Toast.makeText(
//                                context,
//                                "Booking not available",
//                                Toast.LENGTH_LONG
//                            )
//                                .show()
//                        }

//                    }
//                }
//                if (response.code() == 401) {
//
//                    CustomMethods().logOutUser(context!!)
//
//                }
//            }
//
//        })

    }

    private fun checkUserBookingStatus(
        locationId: String,
        selectedPrice: String,
        start_time: String,
        end_time: String,
        type: String,
        selected_date: String,
        selected_instructor: String?,
        selected_board: String,
        selectedIt: String
    ) {

        if (map.isRechedule) {

            Log.e("macro", "asd")
            updateSession(
                locationId,
                selectedPrice,
                start_time,
                end_time,
                type,
                selected_date,
                selected_instructor,
                selected_board,
                locationId
            )

        } else {
            val dialog: Dialog = CustomLoader().loader(context)
            val api: Api = Connection().getCon(context!!)
            //check booking is allowed or not
            call = api.checkBookingSession("Bearer " + Preference().getUserData(context!!)?.token)

            call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    dialog.dismiss()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    dialog.dismiss()
                    Log.e("user_booking_res", "" + response.body())


                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            var data = response.body()!!.get("data").asJsonObject
                            var allowed = data.get("allowed").asString

                            if (allowed.equals("true")) {

                                if (type.equals("normal")) {

                                    var sp: Int = (spinner_timing.selectedItem.toString()).toInt()
                                    var total_costing = (selected_price.toFloat() * sp)

                                    Log.e("total_costing", "" + total_costing)

                                    paymentData = ArrayList()
                                    val payment = PaymentDetails(
                                        "" + type,
                                        "" + total_costing,
                                        "" + selected_date,
                                        "",
                                        "" + locationId, "",
                                        "", "" + selectedIt, "" + start_time, "" + end_time,
                                        null, "" + selected_board
                                    )
                                    paymentData!!.add(payment)

                                    CustomMethods().openPagePaymentDetails(
                                        activity!!,
                                        MainActivity().sessionPaymentFragment, payment
                                    )

                                } else {

                                    paymentData = ArrayList()
                                    val payment = PaymentDetails(
                                        "" + type,
                                        "" + selectedPrice,
                                        "" + selected_date,
                                        "",
                                        "" + locationId, "",
                                        "", "" + selectedIt, "" + start_time, "" + end_time,
                                        "" + selected_instructor, "" + selected_board
                                    )
                                    paymentData!!.add(payment)

                                    CustomMethods().openPagePaymentDetails(
                                        activity!!,
                                        MainActivity().sessionPaymentFragment, payment
                                    )

                                }


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

                        CustomMethods().logOutUser(context!!)

                    }
                }

            })
        }
    }

    private fun clearDate() {
        txt11.text = ""
        txt12.text = ""
        txt13.text = ""
        txt14.text = ""
        txt15.text = ""
        txt16.text = ""
        txt17.text = ""

        txt21.text = ""
        txt22.text = ""
        txt23.text = ""
        txt24.text = ""
        txt25.text = ""
        txt26.text = ""
        txt27.text = ""

        txt31.text = ""
        txt32.text = ""
        txt33.text = ""
        txt34.text = ""
        txt35.text = ""
        txt36.text = ""
        txt37.text = ""

        txt41.text = ""
        txt42.text = ""
        txt43.text = ""
        txt44.text = ""
        txt45.text = ""
        txt46.text = ""
        txt47.text = ""

        txt51.text = ""
        txt52.text = ""
        txt53.text = ""
        txt54.text = ""
        txt55.text = ""
        txt56.text = ""
        txt57.text = ""

        txt61.text = ""
        txt62.text = ""
        txt63.text = ""
        txt64.text = ""
        txt65.text = ""
        txt66.text = ""
        txt67.text = ""
    }

    private fun getGenderSpinner() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGender.adapter = adapter
        }
    }

    private fun wavierAccepted() {

        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.waiverAccepted(
            "Bearer " + Preference().getUserData(context!!)?.token,
            "yes",
            spinnerGender.selectedItem.toString(),
            etAge.text.toString(),
            etWeight.text.toString(),
            selectedRadioButtonValue,
            expericenceDescription
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val data = response.body()!!.get("data").asJsonObject
                            Log.e("macro", "data->$data")
                            setDescFromApi()


                        }
                    } else {
                        Toast.makeText(context, "Please enter details!!", Toast.LENGTH_LONG).show()
                        builder.show()
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

    override fun onTimeSet(
        view: com.wdullaer.materialdatetimepicker.time.TimePickerDialog?,
        hourOfDay: Int,
        minute: Int,
        second: Int
    ) {

        var timing_interval =
            hourOfDay.toString() + ":" + minute.toString() + ":" + second.toString()

        Log.e("timing_interval", "" + timing_interval)

        var cal_time = SimpleDateFormat("HH:mm:ss").parse(timing_interval)
        cal = Calendar.getInstance()
        cal.time = cal_time

        var inTime =
            SimpleDateFormat("HH:mm:ss").parse(
                activehours_start_time
            )
        calendar1 = Calendar.getInstance()
        calendar1.time = inTime

        var c_date = SimpleDateFormat("HH:mm:ss").format(cal.time)
        Log.e("cdate", "" + c_date)
        start_time_24 = c_date
        //Current Time

        var checkTime = SimpleDateFormat("HH:mm:ss").parse(c_date)
        calendar3 = Calendar.getInstance()
        calendar3.time = checkTime

        //End Time
        var finTime =
            SimpleDateFormat("HH:mm:ss").parse(activehours_end_time)
        calendar2 = Calendar.getInstance()
        calendar2.time = finTime

        if (activehours_end_time.compareTo(activehours_start_time) < 0) {
            calendar2.add(Calendar.DATE, 1)
            calendar3.add(Calendar.DATE, 1)
        }

        if ((checkTime.after(calendar1.time) ||
                    checkTime.compareTo(calendar1.time) == 0) &&
            checkTime.before(calendar2.time)
        ) {

            var sdf = SimpleDateFormat("hh:mm:ss")
            var sdfs = SimpleDateFormat("hh:mm a")

            var c_dates: Date = sdf.parse(c_date)
            Log.e("c_dates", "" + c_dates)
            Log.e("c_date", "" + sdfs.format(c_dates))

            var current_cal = Calendar.getInstance()
            val time =
                "" + current_cal.get(Calendar.HOUR_OF_DAY).toString() + ":" + current_cal.get(
                    Calendar.MINUTE
                ).toString() + ":" + current_cal.get(
                    Calendar.SECOND
                )


            var current_date =
                current_cal.get(Calendar.MONTH)
                    .toString() + "-" + current_cal.get(Calendar.DAY_OF_MONTH)
                    .toString() + "-" + current_cal.get(
                    Calendar.YEAR
                ).toString()

            val inFormat =
                SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
            val date1 = inFormat.parse(selected_dates)

            var date2 = inFormat.parse(current_date)
            if (date1.compareTo(date2) == 0) {

                if (c_date.compareTo(time) > 0) {
                    txt_start_time.text = sdfs.format(c_dates).toString()
                    btn_session_popup_submit.isEnabled = true
                } else {
                    txt_start_time.text = "Select"
                    txt_end_time.text = "Select"
                    Toast.makeText(
                        context,
                        "Not a valid time, expecting",
                        Toast.LENGTH_LONG
                    ).show()
                    btn_session_popup_submit.isEnabled = false

                }
            } else {
                txt_start_time.text = sdfs.format(c_dates).toString()
            }



            if (selected_duration.equals("")) {
                selected_duration = "0"
            }
            if (type.equals("discovery_lesson")) {
                lin_selected_time.visibility = View.GONE
                calendar3.add(
                    Calendar.MINUTE,
                    selected_duration.toInt()
                )

            }
            if ((type.equals("cruiser")) || (type.equals("explorer")) || (type.equals(
                    "sport"
                )) || (type.equals("pro")) || (type.equals("certificate"))
            ) {

                lin_selected_time.visibility = View.GONE
                calendar3.add(Calendar.MINUTE, 120)
            }
            if (type.equals("normal")) {

                lin_selected_time.visibility = View.VISIBLE
                if (model!!.isRechedule) {

                    var data: JSONObject = JSONObject(map.response)
                    var timmingArrya = resources.getStringArray(R.array.no_of_timing)
                    spinner_timing.setSelection(
                        timmingArrya.indexOf(
                            data.getJSONObject("data").getString("duration")
                        )
                    )
                    spinner_timing.isClickable = false
                    spinner_timing.isEnabled = false

                    var selected_time =
                        spinner_timing.selectedItem.toString()
                    Log.e(
                        "selected_time",
                        "" + selected_time
                    )
                    var c_date = SimpleDateFormat("HH:mm:ss").format(cal.time)
                    Log.e("cdate", "" + c_date)
                    start_time_24 = c_date
                    //Current Time

                    var checkTime = SimpleDateFormat("HH:mm:ss").parse(c_date)
                    calendar3 = Calendar.getInstance()
                    calendar3.time = checkTime

                    Log.e("caledarTime", "" + calendar3.time)
                    calendar3.add(
                        Calendar.MINUTE,
                        selected_time.toInt()
                    )

                    Log.e("calendar3", "" + calendar3.time)
                    var new_df =
                        SimpleDateFormat("HH:mm:ss")

                    var added_time =
                        new_df.format(calendar3.time)


                    if (calendar3.time.compareTo(calendar2.time) <= 0) {

                        var sdf =
                            SimpleDateFormat("hh:mm:ss")
                        var sdfs =
                            SimpleDateFormat("hh:mm a")

                        var added_times: Date =
                            sdf.parse(added_time)

                        var enddd =
                            SimpleDateFormat("HH:mm:ss").format(
                                added_times
                            )
                        Log.e("enddd", "" + enddd)
                        end_time_24 = enddd

                        txt_end_time.text =
                            sdfs.format(added_times)
                                .toString()

                        btn_session_popup_submit.isEnabled =
                            true

                        btn_session_popup_submit.setOnClickListener {

                            txt_start_time.isEnabled = false
                            btn_session_popup_submit.isEnabled =
                                false
                            getTimeData(
                                locationId,
                                dayName,
                                sessionDate,
                                c_date,
                                added_time
                            )
                        }

                    } else {
                        txt_end_time.text = "Select"
                        Toast.makeText(
                            context,
                            "Please Select Valid Time",
                            Toast.LENGTH_LONG
                        ).show()
                        btn_session_popup_submit.isEnabled =
                            false
                    }

                }

                spinner_timing.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {


                            var selected_time =
                                spinner_timing.selectedItem.toString()
                            Log.e(
                                "selected_time",
                                "" + selected_time
                            )
                            var c_date = SimpleDateFormat("HH:mm:ss").format(cal.time)
                            Log.e("cdate", "" + c_date)
                            start_time_24 = c_date
                            //Current Time

                            var checkTime = SimpleDateFormat("HH:mm:ss").parse(c_date)
                            calendar3 = Calendar.getInstance()
                            calendar3.time = checkTime

                            Log.e("caledarTime", "" + calendar3.time)
                            calendar3.add(
                                Calendar.MINUTE,
                                selected_time.toInt()
                            )

                            Log.e("calendar3", "" + calendar3.time)
                            var new_df =
                                SimpleDateFormat("HH:mm:ss")

                            var added_time =
                                new_df.format(calendar3.time)
                            Log.e("calendar2Time", "" + calendar2.time)
                            if (calendar3.time.compareTo(calendar2.time) <= 0) {

                                var sdf =
                                    SimpleDateFormat("hh:mm:ss")
                                var sdfs =
                                    SimpleDateFormat("hh:mm a")

                                var added_times: Date =
                                    sdf.parse(added_time)

                                var enddd =
                                    SimpleDateFormat("HH:mm:ss").format(
                                        added_times
                                    )
                                Log.e("enddd", "" + enddd)
                                end_time_24 = enddd

                                txt_end_time.text =
                                    sdfs.format(added_times)
                                        .toString()

                                btn_session_popup_submit.isEnabled =
                                    true

                                btn_session_popup_submit.setOnClickListener {

                                    txt_start_time.isEnabled = false
                                    btn_session_popup_submit.isEnabled =
                                        false
                                    getTimeData(
                                        locationId,
                                        dayName,
                                        sessionDate,
                                        c_date,
                                        added_time
                                    )
                                }

                            } else {
                                txt_end_time.text = "Select"
                                Toast.makeText(
                                    context,
                                    "Please Select Valid Time",
                                    Toast.LENGTH_LONG
                                ).show()
                                btn_session_popup_submit.isEnabled =
                                    false
                            }
                        }


                    }


            } else {
//                var sa = context!!.resources.getStringArray(R.array.no_of_timing)
//
//                var selected_timing_data = sa[spinner_timing.selectedItemPosition]
//
//                Log.e("selected_timing_data", "" + selected_timing_data)

                var new_df = SimpleDateFormat("HH:mm:ss")

                var added_time = new_df.format(calendar3.time)

                if (calendar3.time.compareTo(calendar2.time) <= 0) {

                    var sdf = SimpleDateFormat("hh:mm:ss")
                    var sdfs = SimpleDateFormat("hh:mm a")

                    var added_times: Date = sdf.parse(added_time)

                    var enddd =
                        SimpleDateFormat("HH:mm:ss").format(
                            added_times
                        )
                    Log.e("enddd", "" + enddd)
                    end_time_24 = enddd

                    txt_end_time.text =
                        sdfs.format(added_times).toString()

                    btn_session_popup_submit.isEnabled = true

                    btn_session_popup_submit.setOnClickListener {

                        txt_start_time.isEnabled = false
                        btn_session_popup_submit.isEnabled = false
                        getTimeData(
                            locationId,
                            dayName,
                            sessionDate,
                            c_date,
                            added_time
                        )
                    }

                } else {
                    txt_end_time.text = "Select"
                    Toast.makeText(
                        context,
                        "Please Select Valid Time",
                        Toast.LENGTH_LONG
                    ).show()
                    btn_session_popup_submit.isEnabled = false
                }
            }
        } else {
            Toast.makeText(context, "Please Select Valid Time", Toast.LENGTH_LONG).show()
            txt_end_time.text = "Select"
            txt_start_time.text = "Select"
        }

    }


}
