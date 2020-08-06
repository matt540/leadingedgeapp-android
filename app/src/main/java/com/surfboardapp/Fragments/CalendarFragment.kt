package com.surfboardapp.Fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.CalendarPreviousSessionAdapter
import com.surfboardapp.Adapter.CalendarSessionAdapter
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CalendarSession
import com.surfboardapp.Models.PreviousCalendarSession
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList


class CalendarFragment : Fragment() {

    private lateinit var call: Call<JsonObject>
    private lateinit var tvScreenTitle: TextView
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

    lateinit var locationId: String

    private lateinit var cYear: String
    lateinit var tvNodataAvailable: TextView
    var dateUpComing = ""
    var datePrevious = ""
    private val calendar = Calendar.getInstance()
    private lateinit var monthNumber: String
    private lateinit var monthdate: SimpleDateFormat
    private var sessionList: ArrayList<CalendarSession> = ArrayList()
    lateinit var adapter: CalendarSessionAdapter
    lateinit var previousAdapter: CalendarPreviousSessionAdapter
    lateinit var recyclerEvent: RecyclerView
    lateinit var recyclerPreviousSession: RecyclerView
    lateinit var btnBooksession: Button
    private lateinit var tabLayout: TabLayout
    lateinit var previousSessionList: ArrayList<PreviousCalendarSession>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    private lateinit var drawerLayout: DrawerLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        setDrawerSelection()
        setBottomSelection()

        //upcoming sessions
        upComingSessionApi()


        onclick()
    }


    private fun setBottomSelection() {

        val dashboardImage = activity!!.findViewById<ImageView>(R.id.image_session)
        val dashboardText = activity!!.findViewById<TextView>(R.id.text_session)
        dashboardText?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        dashboardImage?.setColorFilter(ContextCompat.getColor(context!!, R.color.black))

        val dashboardImage1 = activity!!.findViewById<ImageView>(R.id.image_dashboard)
        val dashboardText1 = activity!!.findViewById<TextView>(R.id.text_dashboard)
        dashboardText1?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        dashboardImage1?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))

        val dashboardImage2 = activity!!.findViewById<ImageView>(R.id.image_location)
        val dashboardText2 = activity!!.findViewById<TextView>(R.id.text_location)
        dashboardText2?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        dashboardImage2?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))

        val dashboardImage3 = activity!!.findViewById<ImageView>(R.id.image_progression)
        val dashboardText3 = activity!!.findViewById<TextView>(R.id.text_progression)
        dashboardText3?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        dashboardImage3?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))

        val dashboardImage4 = activity!!.findViewById<ImageView>(R.id.image_myAccount)
        val dashboardText4 = activity!!.findViewById<TextView>(R.id.text_myAccount)
        dashboardText4?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        dashboardImage4?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
    }

    private fun setDrawerSelection() {
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_cal_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_cal_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_cal_txt)

        home?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        homeImg?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        hometxt?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
    }

    private fun onclick() {

        btnBooksession.setOnClickListener {
            CustomMethods().openPage(activity!!, MainActivity().locationFinder)
            (activity as MainActivity).clearSelection()

        }


    }

    private fun init(view: View) {
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)

        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_cal_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_cal_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_cal_txt)

        home?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        homeImg?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        hometxt?.setTextColor(ContextCompat.getColor(context!!, R.color.white))

        activity!!.bottom_navigation.visibility = View.VISIBLE
        activity!!.back.visibility = View.GONE
        activity!!.menu.visibility = View.VISIBLE

        txt11 = view.findViewById(R.id.txt11)
        txt12 = view.findViewById(R.id.txt12)
        txt13 = view.findViewById(R.id.txt13)
        txt14 = view.findViewById(R.id.txt14)
        txt15 = view.findViewById(R.id.txt15)
        txt16 = view.findViewById(R.id.txt16)
        txt17 = view.findViewById(R.id.txt17)
        txt21 = view.findViewById(R.id.txt21)
        txt22 = view.findViewById(R.id.txt22)
        txt23 = view.findViewById(R.id.txt23)
        txt24 = view.findViewById(R.id.txt24)
        txt25 = view.findViewById(R.id.txt25)
        txt26 = view.findViewById(R.id.txt26)
        txt27 = view.findViewById(R.id.txt27)
        txt31 = view.findViewById(R.id.txt31)
        txt32 = view.findViewById(R.id.txt32)
        txt33 = view.findViewById(R.id.txt33)
        txt34 = view.findViewById(R.id.txt34)
        txt35 = view.findViewById(R.id.txt35)
        txt36 = view.findViewById(R.id.txt36)
        txt37 = view.findViewById(R.id.txt37)
        txt41 = view.findViewById(R.id.txt41)
        txt42 = view.findViewById(R.id.txt42)
        txt43 = view.findViewById(R.id.txt43)
        txt44 = view.findViewById(R.id.txt44)
        txt45 = view.findViewById(R.id.txt45)
        txt46 = view.findViewById(R.id.txt46)
        txt47 = view.findViewById(R.id.txt47)
        txt51 = view.findViewById(R.id.txt51)
        txt52 = view.findViewById(R.id.txt52)
        txt53 = view.findViewById(R.id.txt53)
        txt54 = view.findViewById(R.id.txt54)
        txt55 = view.findViewById(R.id.txt55)
        txt56 = view.findViewById(R.id.txt56)
        txt57 = view.findViewById(R.id.txt57)
        txt61 = view.findViewById(R.id.txt61)
        txt62 = view.findViewById(R.id.txt62)
        txt63 = view.findViewById(R.id.txt63)
        txt64 = view.findViewById(R.id.txt64)
        txt65 = view.findViewById(R.id.txt65)
        txt66 = view.findViewById(R.id.txt66)
        txt67 = view.findViewById(R.id.txt67)

        lin11 = view.findViewById(R.id.lin11)
        lin12 = view.findViewById(R.id.lin12)
        lin13 = view.findViewById(R.id.lin13)
        lin14 = view.findViewById(R.id.lin14)
        lin15 = view.findViewById(R.id.lin15)
        lin16 = view.findViewById(R.id.lin16)
        lin17 = view.findViewById(R.id.lin17)
        lin21 = view.findViewById(R.id.lin21)
        lin22 = view.findViewById(R.id.lin22)
        lin23 = view.findViewById(R.id.lin23)
        lin24 = view.findViewById(R.id.lin24)
        lin25 = view.findViewById(R.id.lin25)
        lin26 = view.findViewById(R.id.lin26)
        lin27 = view.findViewById(R.id.lin27)
        lin31 = view.findViewById(R.id.lin31)
        lin32 = view.findViewById(R.id.lin32)
        lin33 = view.findViewById(R.id.lin33)
        lin34 = view.findViewById(R.id.lin34)
        lin35 = view.findViewById(R.id.lin35)
        lin36 = view.findViewById(R.id.lin36)
        lin37 = view.findViewById(R.id.lin37)
        lin41 = view.findViewById(R.id.lin41)
        lin42 = view.findViewById(R.id.lin42)
        lin43 = view.findViewById(R.id.lin43)
        lin44 = view.findViewById(R.id.lin44)
        lin45 = view.findViewById(R.id.lin45)
        lin46 = view.findViewById(R.id.lin46)
        lin47 = view.findViewById(R.id.lin47)
        lin51 = view.findViewById(R.id.lin51)
        lin52 = view.findViewById(R.id.lin52)
        lin53 = view.findViewById(R.id.lin53)
        lin54 = view.findViewById(R.id.lin54)
        lin55 = view.findViewById(R.id.lin55)
        lin56 = view.findViewById(R.id.lin56)
        lin57 = view.findViewById(R.id.lin57)
        lin61 = view.findViewById(R.id.lin61)
        lin62 = view.findViewById(R.id.lin62)
        lin63 = view.findViewById(R.id.lin63)
        lin64 = view.findViewById(R.id.lin64)
        lin65 = view.findViewById(R.id.lin65)
        lin66 = view.findViewById(R.id.lin66)
        lin67 = view.findViewById(R.id.lin67)

        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.calendar)
        recyclerEvent = view.findViewById(R.id.recycler_event)

        tvNodataAvailable = view.findViewById(R.id.tv_noDataAvailable)
        btnBooksession = view.findViewById(R.id.btn_bookSession)
        recyclerPreviousSession = view.findViewById(R.id.recycler_previousSession)
        tabLayout = view.findViewById(R.id.tabLayout)
        previousSessionList = ArrayList()


        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    recyclerEvent.visibility = View.VISIBLE
                    recyclerPreviousSession.visibility = View.GONE
                    //upcoming sessions
                    upComingSessionApi()
                    displayDateUpComingSession(calendar)

                    img_right_arrow.setOnClickListener {
                        calendar.add(Calendar.MONTH, 1)
                        displayDateUpComingSession(calendar)

                    }
                    img_left_arrow.setOnClickListener {
                        calendar.add(Calendar.MONTH, -1)
                        displayDateUpComingSession(calendar)
                    }
                }
                if (tab.position == 1) {
                    recyclerEvent.visibility = View.GONE
                    recyclerPreviousSession.visibility = View.VISIBLE
                    //previous session
                    previousSessionApi()
                    displayDatePreviousSession(calendar)
                    img_right_arrow.setOnClickListener {
                        calendar.add(Calendar.MONTH, 1)
                        displayDatePreviousSession(calendar)

                    }
                    img_left_arrow.setOnClickListener {
                        calendar.add(Calendar.MONTH, -1)
                        displayDatePreviousSession(calendar)
                    }
                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun displayDateUpComingSession(calendar: Calendar) {


        cYear = calendar.get(Calendar.YEAR).toString()
        txt_year.text = cYear

        monthNumber =
            checkDigit(Integer.valueOf((calendar.get(Calendar.MONTH) + 1).toString()))
        monthdate = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthdate.format(calendar.time)
        txt_month.text = monthName

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
            setDateUpComingSession(i.toString(), column.toString(), calendar)
            j++
        }

    }

    private fun previousSessionApi() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.previousSessions("Bearer " + Preference().getUserData(context!!)?.token)
            .enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)

                    displayDatePreviousSession(calendar)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    CustomLoader().stopLoader(dialog)
//                    try {

                        if (response.isSuccessful) {
                            if (response.body() != null) {

                                val data = response.body()!!.get("data").asJsonArray
                                if (data.size() > 0) {
                                    recyclerPreviousSession.visibility = View.VISIBLE
                                    tvNodataAvailable.visibility = View.GONE
                                    btnBooksession.visibility = View.GONE
                                    previousSessionList.clear()
                                    for (i in 0 until data.size()) {
                                        val jsonObj = data.get(i).asJsonObject

                                        val id = jsonObj.get("id").asString
                                        val type = jsonObj.get("type").asString
                                        val status = jsonObj.get("status").asString
//                                    val status = jsonObj.get("status").asString
                                        datePrevious = jsonObj.get("date").asString
                                        val cost = jsonObj.get("cost").asString
//                                    val locationId1 = jsonObj.get("location_id").asString
//                                    val timeSlotId1 = jsonObj.get("time_slot_id").asString


                                        val location = jsonObj.get("location").asJsonObject
//                                    val locId = location.get("id").asString
                                        val name = location.get("name").asString
//                                    val loc = location.get("location").asString

//                                    val timeSlot = jsonObj.get("time_slot").asJsonObject
//                                    val timeSlotId = timeSlot.get("id").asString
//                                    val day = timeSlot.get("day").asString
//                                    val fromTime = timeSlot.get("from_time").asString
//                                    val toTime = timeSlot.get("to_time").asString


                                        val rating = jsonObj.get("rating").asJsonArray
                                        var ratingFromUser = ""
                                        if (rating.size() > 0) {
                                            for (j in 0 until rating.size()) {
                                                val obj = rating.get(j).asJsonObject
//                                            val ratingId = obj.get("id").asString
//                                            val userId = obj.get("user_id").asString
//                                            val sessionId = obj.get("session_id").asString
                                                ratingFromUser = obj.get("rating").asString
                                            }
                                        }
                                        val previousSessionModel =
                                            PreviousCalendarSession(
                                                type,
                                                datePrevious,
                                                cost,
                                                name,
                                                id,
                                                ratingFromUser,
                                                status
                                            )
                                        previousSessionList.add(previousSessionModel)

                                    }
                                    displayDatePreviousSession(calendar)

                                    previousAdapter = CalendarPreviousSessionAdapter(
                                        activity as MainActivity,
                                        previousSessionList
                                    )
                                    recyclerPreviousSession.adapter = previousAdapter
                                    recyclerPreviousSession.layoutManager =
                                        LinearLayoutManager(context)

                                    previousAdapter.notifyDataSetChanged()

                                } else {
                                    displayDateUpComingSession(calendar)
                                    recyclerPreviousSession.visibility = View.GONE
                                    tvNodataAvailable.visibility = View.VISIBLE
                                    btnBooksession.visibility = View.GONE
                                }
                                displayDatePreviousSession(calendar)
                            }
                        }
                        if (response.code() == 401) {

                            CustomMethods().logOutUser(context!!)

                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show()
//                    }
                }
            })
    }

    private fun upComingSessionApi() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        call = api.upcomingSessions("Bearer " + Preference().getUserData(context!!)?.token)
        call.enqueue(object : Callback,
            retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
                displayDateUpComingSession(calendar)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
//                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val data = response.body()!!.get("data").asJsonArray

                            if (data.size() > 0) {
                                recyclerEvent.visibility = View.VISIBLE
                                tvNodataAvailable.visibility = View.GONE
                                btnBooksession.visibility = View.GONE
                                sessionList.clear()
                                for (i in 0 until data.size()) {
                                    val jsonobject = data.get(i).asJsonObject
                                    val id: String = jsonobject.get("id").asString
                                    val type: String = jsonobject.get("type").asString
//                                val status: String = jsonobject.get("status").asString
                                    dateUpComing = jsonobject.get("date").asString

                                    val cost: String = jsonobject.get("cost").asString
//                                val expectedArrivalTime: String = jsonobject.get("expected_arrival_time").asString
//                                val locationId1: String = jsonobject.get("location_id").asString
                                    val location: JsonObject =
                                        jsonobject.get("location").asJsonObject
                                    locationId = location.get("id").asString

                                    val locationName: String = location.get("name").asString
//                                val locationLocation: String = location.get("location").asString

                                    val start_time = jsonobject.get("start_time").asString

                                    val sessionModel =
                                        CalendarSession(
                                            type,
                                            dateUpComing,
                                            cost,
                                            locationName,
                                            id,
                                            start_time
                                        )
                                    sessionList.add(sessionModel)

                                }
                                displayDateUpComingSession(calendar)
                                adapter = CalendarSessionAdapter(
                                    activity as MainActivity,
                                    sessionList
                                )
                                recyclerEvent.adapter = adapter
                                recyclerEvent.layoutManager =
                                    LinearLayoutManager(context)

                                adapter.notifyDataSetChanged()


                            } else {
                                displayDateUpComingSession(calendar)

                                recyclerEvent.visibility = View.GONE
                                tvNodataAvailable.visibility = View.VISIBLE
                                btnBooksession.visibility = View.VISIBLE
                            }


                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }

//                } catch (e: Exception) {
//                }
            }

        })
    }

    private fun setDateUpComingSession(
        day: String,
        column: String,
        calendar: Calendar
    ) {

        monthdate = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthdate.format(calendar.time)
        txt_month.text = monthName


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
                        getTextFnUpComing(lin17, txt17)
                    }
                    "2" -> {
                        txt27.text = day
                        getTextFnUpComing(lin27, txt27)
                    }
                    "3" -> {
                        txt37.text = day
                        getTextFnUpComing(lin37, txt37)
                    }
                    "4" -> {
                        txt47.text = day
                        getTextFnUpComing(lin47, txt47)
                    }
                    "5" -> {
                        txt57.text = day
                        getTextFnUpComing(lin57, txt57)
                    }
                    "6" -> {
                        txt67.text = day
                        getTextFnUpComing(lin67, txt67)
                    }
                }
                return
            }


            Calendar.MONDAY -> {

                when (column) {
                    "1" -> {
                        txt11.text = day
                        getTextFnUpComing(lin11, txt11)
                    }
                    "2" -> {
                        txt21.text = day
                        getTextFnUpComing(lin21, txt21)
                    }
                    "3" -> {
                        txt31.text = day
                        getTextFnUpComing(lin31, txt31)
                    }
                    "4" -> {
                        txt41.text = day
                        getTextFnUpComing(lin41, txt41)
                    }
                    "5" -> {
                        txt51.text = day
                        getTextFnUpComing(lin51, txt51)
                    }
                    "6" -> {
                        txt61.text = day
                        getTextFnUpComing(lin61, txt61)
                    }
                }
                return
            }


            Calendar.TUESDAY -> {

                when (column) {
                    "1" -> {
                        txt12.text = day
                        getTextFnUpComing(lin12, txt12)
                    }
                    "2" -> {
                        txt22.text = day
                        getTextFnUpComing(lin22, txt22)
                    }
                    "3" -> {
                        txt32.text = day
                        getTextFnUpComing(lin32, txt32)
                    }
                    "4" -> {
                        txt42.text = day
                        getTextFnUpComing(lin42, txt42)
                    }
                    "5" -> {
                        txt52.text = day
                        getTextFnUpComing(lin52, txt52)
                    }
                    "6" -> {
                        txt62.text = day
                        getTextFnUpComing(lin62, txt62)
                    }
                }
                return
            }


            Calendar.WEDNESDAY -> {

                when (column) {
                    "1" -> {
                        txt13.text = day
                        getTextFnUpComing(lin13, txt13)
                    }
                    "2" -> {
                        txt23.text = day
                        getTextFnUpComing(lin23, txt23)
                    }
                    "3" -> {
                        txt33.text = day
                        getTextFnUpComing(lin33, txt33)
                    }
                    "4" -> {
                        txt43.text = day
                        getTextFnUpComing(lin43, txt43)
                    }
                    "5" -> {
                        txt53.text = day
                        getTextFnUpComing(lin53, txt53)
                    }
                    "6" -> {
                        txt63.text = day
                        getTextFnUpComing(lin63, txt63)
                    }
                }
                return
            }


            Calendar.THURSDAY -> {

                when (column) {
                    "1" -> {
                        txt14.text = day
                        getTextFnUpComing(lin14, txt14)
                    }
                    "2" -> {
                        txt24.text = day
                        getTextFnUpComing(lin24, txt24)
                    }
                    "3" -> {
                        txt34.text = day
                        getTextFnUpComing(lin34, txt34)
                    }
                    "4" -> {
                        txt44.text = day
                        getTextFnUpComing(lin44, txt44)
                    }
                    "5" -> {
                        txt54.text = day
                        getTextFnUpComing(lin54, txt54)
                    }
                    "6" -> {
                        txt64.text = day
                        getTextFnUpComing(lin64, txt64)
                    }
                }
                return
            }

            Calendar.FRIDAY -> {

                when (column) {
                    "1" -> {
                        txt15.text = day
                        getTextFnUpComing(lin15, txt15)
                    }
                    "2" -> {
                        txt25.text = day
                        getTextFnUpComing(lin25, txt25)
                    }
                    "3" -> {
                        txt35.text = day
                        getTextFnUpComing(lin35, txt35)
                    }
                    "4" -> {
                        txt45.text = day
                        getTextFnUpComing(lin45, txt45)
                    }
                    "5" -> {
                        txt55.text = day
                        getTextFnUpComing(lin55, txt55)
                    }
                    "6" -> {
                        txt65.text = day
                        getTextFnUpComing(lin65, txt65)
                    }
                }
                return
            }

            Calendar.SATURDAY ->

                when (column) {
                    "1" -> {
                        txt16.text = day
                        getTextFnUpComing(lin16, txt16)
                    }
                    "2" -> {
                        txt26.text = day
                        getTextFnUpComing(lin26, txt26)
                    }
                    "3" -> {
                        txt36.text = day
                        getTextFnUpComing(lin36, txt36)
                    }
                    "4" -> {
                        txt46.text = day
                        getTextFnUpComing(lin46, txt46)
                    }
                    "5" -> {
                        txt56.text = day
                        getTextFnUpComing(lin56, txt56)
                    }
                    "6" -> {
                        txt66.text = day
                        getTextFnUpComing(lin66, txt66)
                    }
                }

        }

    }

    private fun getTextFnUpComing(
        lin11: LinearLayout,
        txt11: TextView
    ) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH] + 1
        val currentDay = calendar[Calendar.DAY_OF_MONTH]
        val todayDate =
            "$currentYear/$currentMonth/$currentDay"

        var currentMonthName = ""
        val splitString = (todayDate).split("/")
        val currentYearNo = splitString[0]
        var monthNo = splitString[1]
        if (monthNo <= 9.toString()) {
            monthNo = ("0$monthNo")
        }
        val currentDateInNo = splitString[2]

        when (monthNo) {
            "01" ->
                currentMonthName = "January"
            "02" ->
                currentMonthName = "February"
            "03" ->
                currentMonthName = "March"
            "04" ->
                currentMonthName = "April"
            "05" ->
                currentMonthName = "May"
            "06" ->
                currentMonthName = "June"
            "07" ->
                currentMonthName = "July"
            "08" ->
                currentMonthName = "August"
            "09" ->
                currentMonthName = "September"
            "10" ->
                currentMonthName = "October"
            "11" ->
                currentMonthName = "November"
            "12" ->
                currentMonthName = "December"
        }


        if (dateUpComing != "") {


            var monthName = ""
            val splitDate = dateUpComing.split("-")
            val year = splitDate[0]
            val month = splitDate[1]
            val daywise = splitDate[2]

            var day = checkDigit(daywise.toInt())

//            var day = ""

//            if (dayWise <= 9.toString()) {
//
//                day =
//                    dayWise.substring(1, dayWise.indexOf("0") + 2)
//            } else {
//                day = dayWise
//            }

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




            if (txt_month.text.toString() == monthName && txt11.text.toString() == day && cYear == year) {
                lin11.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.colorPrimaryDark
                    )
                )
                txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.white))

            } else {
                lin11.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        android.R.color.transparent
                    )
                )
                txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.black))


                if (txt_month.text.toString() == currentMonthName && txt11.text.toString() == currentDateInNo && cYear == currentYearNo) {
                    txt11.setTextColor(
                        ContextCompat.getColorStateList(
                            context!!,
                            R.color.color_red
                        )
                    )
                } else {
                    txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.black))
                }
            }
        } else {
            if (txt_month.text.toString() == currentMonthName && txt11.text.toString() == currentDateInNo && cYear == currentYearNo) {
                txt11.setTextColor(
                    ContextCompat.getColorStateList(
                        context!!,
                        R.color.color_red
                    )
                )
            } else {
                txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.black))
            }
        }
    }

    private fun clearDate() {
        txt11.text = ""
        lin11.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt12.text = ""
        lin12.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt13.text = ""
        lin13.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt14.text = ""
        lin14.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt15.text = ""
        lin15.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt16.text = ""
        lin16.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt17.text = ""
        lin17.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )

        txt21.text = ""
        lin21.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt22.text = ""
        lin22.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt23.text = ""
        lin23.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt24.text = ""
        lin24.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt25.text = ""
        lin25.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt26.text = ""
        lin26.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt27.text = ""
        lin27.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )

        txt31.text = ""
        lin31.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt32.text = ""
        lin32.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt33.text = ""
        lin33.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt34.text = ""
        lin34.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt35.text = ""
        lin35.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt36.text = ""
        lin36.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt37.text = ""
        lin37.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )

        txt41.text = ""
        lin41.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt42.text = ""
        lin42.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt43.text = ""
        lin43.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt44.text = ""
        lin44.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt45.text = ""
        lin45.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt46.text = ""
        lin46.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt47.text = ""
        lin47.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )

        txt51.text = ""
        lin51.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt52.text = ""
        lin52.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt53.text = ""
        lin53.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt54.text = ""
        lin54.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt55.text = ""
        lin55.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt56.text = ""
        lin56.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt57.text = ""
        lin57.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )

        txt61.text = ""
        lin61.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt62.text = ""
        lin62.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt63.text = ""
        lin63.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt64.text = ""
        lin64.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt65.text = ""
        lin65.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt66.text = ""
        lin66.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
        txt67.text = ""
        lin67.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
    }

    private fun checkDigit(number: Int): String {

        return if (number <= 9) "0$number" else number.toString()

    }

    private fun displayDatePreviousSession(calendar: Calendar) {


        cYear = calendar.get(Calendar.YEAR).toString()
        txt_year.text = cYear

        monthNumber =
            checkDigit(Integer.valueOf((calendar.get(Calendar.MONTH) + 1).toString()))
        monthdate = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthdate.format(calendar.time)
        txt_month.text = monthName

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
            setDatePreviousSession(i.toString(), column.toString(), calendar)
            j++
        }

    }

    private fun setDatePreviousSession(
        day: String,
        column: String,
        calendar: Calendar
    ) {

        monthdate = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthdate.format(calendar.time)
        txt_month.text = monthName


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
                        getTextFnPrevious(lin17, txt17)
                    }
                    "2" -> {
                        txt27.text = day
                        getTextFnPrevious(lin27, txt27)
                    }
                    "3" -> {
                        txt37.text = day
                        getTextFnPrevious(lin37, txt37)
                    }
                    "4" -> {
                        txt47.text = day
                        getTextFnPrevious(lin47, txt47)
                    }
                    "5" -> {
                        txt57.text = day
                        getTextFnPrevious(lin57, txt57)
                    }
                    "6" -> {
                        txt67.text = day
                        getTextFnPrevious(lin67, txt67)
                    }
                }
                return
            }


            Calendar.MONDAY -> {

                when (column) {
                    "1" -> {
                        txt11.text = day
                        getTextFnPrevious(lin11, txt11)
                    }
                    "2" -> {
                        txt21.text = day
                        getTextFnPrevious(lin21, txt21)
                    }
                    "3" -> {
                        txt31.text = day
                        getTextFnPrevious(lin31, txt31)
                    }
                    "4" -> {
                        txt41.text = day
                        getTextFnPrevious(lin41, txt41)
                    }
                    "5" -> {
                        txt51.text = day
                        getTextFnPrevious(lin51, txt51)
                    }
                    "6" -> {
                        txt61.text = day
                        getTextFnPrevious(lin61, txt61)
                    }
                }
                return
            }


            Calendar.TUESDAY -> {

                when (column) {
                    "1" -> {
                        txt12.text = day
                        getTextFnPrevious(lin12, txt12)
                    }
                    "2" -> {
                        txt22.text = day
                        getTextFnPrevious(lin22, txt22)
                    }
                    "3" -> {
                        txt32.text = day
                        getTextFnPrevious(lin32, txt32)
                    }
                    "4" -> {
                        txt42.text = day
                        getTextFnPrevious(lin42, txt42)
                    }
                    "5" -> {
                        txt52.text = day
                        getTextFnPrevious(lin52, txt52)
                    }
                    "6" -> {
                        txt62.text = day
                        getTextFnPrevious(lin62, txt62)
                    }
                }
                return
            }


            Calendar.WEDNESDAY -> {

                when (column) {
                    "1" -> {
                        txt13.text = day
                        getTextFnPrevious(lin13, txt13)
                    }
                    "2" -> {
                        txt23.text = day
                        getTextFnPrevious(lin23, txt23)
                    }
                    "3" -> {
                        txt33.text = day
                        getTextFnPrevious(lin33, txt33)
                    }
                    "4" -> {
                        txt43.text = day
                        getTextFnPrevious(lin43, txt43)
                    }
                    "5" -> {
                        txt53.text = day
                        getTextFnPrevious(lin53, txt53)
                    }
                    "6" -> {
                        txt63.text = day
                        getTextFnPrevious(lin63, txt63)
                    }
                }
                return
            }


            Calendar.THURSDAY -> {

                when (column) {
                    "1" -> {
                        txt14.text = day
                        getTextFnPrevious(lin14, txt14)
                    }
                    "2" -> {
                        txt24.text = day
                        getTextFnPrevious(lin24, txt24)
                    }
                    "3" -> {
                        txt34.text = day
                        getTextFnPrevious(lin34, txt34)
                    }
                    "4" -> {
                        txt44.text = day
                        getTextFnPrevious(lin44, txt44)
                    }
                    "5" -> {
                        txt54.text = day
                        getTextFnPrevious(lin54, txt54)
                    }
                    "6" -> {
                        txt64.text = day
                        getTextFnPrevious(lin64, txt64)
                    }
                }
                return
            }

            Calendar.FRIDAY -> {

                when (column) {
                    "1" -> {
                        txt15.text = day
                        getTextFnPrevious(lin15, txt15)
                    }
                    "2" -> {
                        txt25.text = day
                        getTextFnPrevious(lin25, txt25)
                    }
                    "3" -> {
                        txt35.text = day
                        getTextFnPrevious(lin35, txt35)
                    }
                    "4" -> {
                        txt45.text = day
                        getTextFnPrevious(lin45, txt45)
                    }
                    "5" -> {
                        txt55.text = day
                        getTextFnPrevious(lin55, txt55)
                    }
                    "6" -> {
                        txt65.text = day
                        getTextFnPrevious(lin65, txt65)
                    }
                }
                return
            }

            Calendar.SATURDAY ->

                when (column) {
                    "1" -> {
                        txt16.text = day
                        getTextFnPrevious(lin16, txt16)
                    }
                    "2" -> {
                        txt26.text = day
                        getTextFnPrevious(lin26, txt26)
                    }
                    "3" -> {
                        txt36.text = day
                        getTextFnPrevious(lin36, txt36)
                    }
                    "4" -> {
                        txt46.text = day
                        getTextFnPrevious(lin46, txt46)
                    }
                    "5" -> {
                        txt56.text = day
                        getTextFnPrevious(lin56, txt56)
                    }
                    "6" -> {
                        txt66.text = day
                        getTextFnPrevious(lin66, txt66)
                    }
                }

        }

    }

    private fun getTextFnPrevious(
        lin11: LinearLayout,
        txt11: TextView
    ) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH] + 1
        val currentDay = calendar[Calendar.DAY_OF_MONTH]
        val todayDate =
            "$currentYear/$currentMonth/$currentDay"

        var currentMonthName = ""
        val splitString = (todayDate).split("/")
        val currentYearNo = splitString[0]
        var monthNo = splitString[1]
        if (monthNo <= 9.toString()) {
            monthNo = ("0$monthNo")
        }
        val currentDateInNo = splitString[2]

        when (monthNo) {
            "01" ->
                currentMonthName = "January"
            "02" ->
                currentMonthName = "February"
            "03" ->
                currentMonthName = "March"
            "04" ->
                currentMonthName = "April"
            "05" ->
                currentMonthName = "May"
            "06" ->
                currentMonthName = "June"
            "07" ->
                currentMonthName = "July"
            "08" ->
                currentMonthName = "August"
            "09" ->
                currentMonthName = "September"
            "10" ->
                currentMonthName = "October"
            "11" ->
                currentMonthName = "November"
            "12" ->
                currentMonthName = "December"
        }


        if (datePrevious != "") {

            Log.e("datePrevious", "" + datePrevious)
            var monthName = ""
            val splitDate = datePrevious.split("-")
            val year = splitDate[0]
            val month = splitDate[1]
            val dayWise = splitDate[2]
            val day: String = checkDigit(dayWise.toInt())


//            day = if (dayWise <= 9.toString()) {
//
//                dayWise.substring(1, dayWise.indexOf("0") + 2)
//            } else {
//                dayWise
//            }


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




            Log.e("day", "" + day)
            if (txt_month.text.toString() == monthName && txt11.text.toString() == day && cYear == year) {
                lin11.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.colorPrimaryDark
                    )
                )
                txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.white))

            } else {
                lin11.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        android.R.color.transparent
                    )
                )
                txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.black))


                if (txt_month.text.toString() == currentMonthName && txt11.text.toString() == currentDateInNo && cYear == currentYearNo) {
                    txt11.setTextColor(
                        ContextCompat.getColorStateList(
                            context!!,
                            R.color.color_red
                        )
                    )
                } else {
                    txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.black))
                }
            }
        } else {
            if (txt_month.text.toString() == currentMonthName && txt11.text.toString() == currentDateInNo && cYear == currentYearNo) {
                txt11.setTextColor(
                    ContextCompat.getColorStateList(
                        context!!,
                        R.color.color_red
                    )
                )
            } else {
                txt11.setTextColor(ContextCompat.getColorStateList(context!!, R.color.black))
            }
        }
    }
}
