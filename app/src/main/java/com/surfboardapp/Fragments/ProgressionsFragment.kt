package com.surfboardapp.Fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.*
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.*
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ProgressionsFragment : Fragment() {
    private lateinit var call: Call<JsonObject>
    private lateinit var tvScreenTitle: TextView
    var progressionId: String = ""
    var status: String = ""
    var statusOne: String = ""
    var status_typeOne: String = "false"
    var status_type: String = "false"
    var status2: String = ""
    var status_type2: String = "false"
    var status3: String = ""
    var status_type3: String = "false"
    var status4: String = ""
    var status_type4: String = "false"
    var cost: String = ""
    var progressionType: String = ""
    var date: String = ""
    var locationId: String = ""
    var comment: String = ""
    var commentedBy: String = ""
    var pId: String = ""
    var paymentModelId: String = ""
    var paymentId: String = ""
    var instructorId: String = ""
    var instructorName: String = ""
    var locId: String = ""
    var locName: String = ""
    lateinit var progressionEnrollment: Any
    lateinit var progressionLevel1: Any
    lateinit var discoveryLession: Any
    lateinit var progressionLevel2: Any
    lateinit var progressionLevel3: Any
    lateinit var progressionLevel4: Any
    val progressionEnrollmentList: ArrayList<Progression_Enrollment> = ArrayList()
    val discoveryLessonList: ArrayList<DiscoveryLesson> = ArrayList()
    val progressionLevel1List: ArrayList<ProgressionLevel1> = ArrayList()
    val progressionLevel2List: ArrayList<ProgressionLevel2> = ArrayList()
    val progressionLevel3List: ArrayList<ProgressionLevel3> = ArrayList()
    val progressionLevel4List: ArrayList<ProgressionLevel4> = ArrayList()
    lateinit var adapterEnrollment: ProgressionModelAdapter
    lateinit var discoveryAdapter: DiscoveryAdapter
    lateinit var adapterLevel1: ProgressionLevel1Adapter
    lateinit var adapterLevel2: ProgressionLevel2Adapter
    lateinit var adapterLevel3: ProgressionLevel3Adapter
    lateinit var adapterLevel4: ProgressionLevel4Adapter
    lateinit var recyclerProgressionenrollment: RecyclerView
    lateinit var recyclerProgressionlevel1: RecyclerView
    lateinit var recyclerProgressionlevel2: RecyclerView
    lateinit var recyclerProgressionlevel3: RecyclerView
    lateinit var recyclerProgressionlevel4: RecyclerView
    lateinit var recycler_progressionLevel_discovery: RecyclerView
    lateinit var tvProgressionmember: TextView
    lateinit var tv_progressionMember_membership_text: TextView
    lateinit var linear_progressionLevel1: LinearLayout
    lateinit var linear_progressionLevel2: LinearLayout
    lateinit var linear_progressionLevel3: LinearLayout
    lateinit var linear_progressionLevel4: LinearLayout
    lateinit var linear_recycler_progression: LinearLayout
    lateinit var tv_progression_title1: TextView
    lateinit var tv_progression_title2: TextView
    lateinit var tv_progression_title3: TextView
    lateinit var tv_progression_title4: TextView
    lateinit var btn_book_now_first: Button
    lateinit var btn_book_now_second: Button
    lateinit var btn_book_now_third: Button
    lateinit var btn_book_now_forth: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progressions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setBottomSelection()

        //For progressions status that is is completed or not
        apiCall()
    }

    private fun setBottomSelection() {

        val dashboardImage = activity!!.findViewById<ImageView>(R.id.image_progression)
        val dashboardText = activity!!.findViewById<TextView>(R.id.text_progression)
        dashboardText?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        dashboardImage?.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
    }

    private fun apiCall() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        call = api.progressionStatus("Bearer " + Preference().getUserData(context!!)?.token)
        call.enqueue(object : Callback,
            retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
                Toast.makeText(context!!, "" + t, Toast.LENGTH_LONG).show()
            }

            @SuppressLint("InflateParams")
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Log.e("progression_res", "" + response.body())

                            val data = response.body()!!.get("data").asJsonObject
                            if (data.has("progression_history")) {


                                val progressionHistory =
                                    data.get("progression_history").asJsonObject

                                //Progression Enrollment

                                if (progressionHistory.has("discovery_enrollment")) {
                                    if (!progressionHistory.get("discovery_enrollment").isJsonNull) {
                                        val progressionEnrollment1 =
                                            progressionHistory.get("discovery_enrollment")
                                                .asJsonArray


                                        if (progressionEnrollment1.size() > 0) {

                                            for (i in 0 until progressionEnrollment1.size()) {
                                                if (!progressionEnrollment1.get(i).isJsonNull) {
                                                    val first =
                                                        progressionEnrollment1.get(i).asJsonObject
                                                    if (first.has("id")) {
                                                        progressionId =
                                                            CustomMethods().getString(first, "id")
                                                    }
                                                    if (first.has("status")) {

                                                        status =
                                                            CustomMethods().getString(
                                                                first,
                                                                "status"
                                                            )
                                                    }
                                                    if (status.equals("1")) {
                                                        status_type = "true"
                                                    }
                                                    if (first.has("cost")) {

                                                        cost =
                                                            CustomMethods().getString(first, "cost")
                                                    }
                                                    if (first.has("type")) {
                                                        progressionType =
                                                            CustomMethods().getString(first, "type")
                                                    }
                                                    if (first.has("date")) {
                                                        date =
                                                            CustomMethods().getString(first, "date")
                                                    }
                                                    if (first.has("location_id")) {
                                                        locationId =
                                                            CustomMethods().getString(
                                                                first,
                                                                "location_id"
                                                            )
                                                    }
                                                    if (first.has("comment")) {
                                                        comment =
                                                            CustomMethods().getString(
                                                                first,
                                                                "comment"
                                                            )
                                                    }
                                                    if (first.has("commented_by")) {
                                                        commentedBy =
                                                            CustomMethods().getString(
                                                                first,
                                                                "commented_by"
                                                            )
                                                    }

//                                            if (first.has("payment")) {
//
//                                                if ((!first.get("payment").isJsonNull)) {
//
//                                                    if (!first.get(
//                                                            "payment"
//                                                        ).asString.equals("")
//                                                    ) {
//                                                        val payment =
//                                                            first.get("payment").asJsonObject
//                                                        pId = payment.get("id").asString
//                                                        paymentModelId =
//                                                            payment.get("model_id").asString
//                                                        paymentId =
//                                                            payment.get("payment_id").asString
//                                                    }
//                                                }
//
//                                            }

                                                    if (first.has("instructor")) {
                                                        if ((!first.get("instructor").isJsonNull)) {
                                                            if (first.get("instructor").asString != ""
                                                            ) {

                                                                val instructor =
                                                                    first.get("instructor")
                                                                        .asJsonObject
                                                                instructorId =
                                                                    instructor.get("id").asString
                                                                instructorName =
                                                                    instructor.get("name").asString
                                                            }
                                                        }
                                                    }
//                                                if (first.has("location")) {
//                                                    if ((!first.get("location").isJsonNull)) {
//                                                        if ((first.get(
//                                                                "location"
//                                                            ).asString != "")
//                                                        ) {

                                                    val location =
                                                        first.get("location").asJsonObject
                                                    locId = location.get("id").asString
                                                    locName =
                                                        location.get("name").asString
//                                                        }
//                                                    }
//                                                }

                                                    progressionEnrollment = Progression_Enrollment(
                                                        progressionId,
                                                        status,
                                                        cost,
                                                        progressionType,
                                                        date,
                                                        locationId,
                                                        comment,
                                                        commentedBy,
                                                        pId,
                                                        paymentModelId,
                                                        paymentId,
                                                        instructorId,
                                                        instructorName,
                                                        locId,
                                                        locName,
                                                        status_type
                                                    )

                                                }
                                                progressionEnrollmentList.add(progressionEnrollment as Progression_Enrollment)


                                            }
                                            adapterEnrollment = ProgressionModelAdapter(
                                                activity as MainActivity,
                                                progressionEnrollmentList
                                            )
                                            recyclerProgressionenrollment.adapter =
                                                adapterEnrollment
                                            recyclerProgressionenrollment.layoutManager =
                                                LinearLayoutManager(context)
                                            val dividerItemDecoration = DividerItemDecoration(
                                                recyclerProgressionenrollment.context,
                                                DividerItemDecoration.VERTICAL
                                            )
                                            recyclerProgressionenrollment.addItemDecoration(
                                                dividerItemDecoration
                                            )

                                            adapterEnrollment.notifyDataSetChanged()


                                        }
                                    }
                                }


                                if (progressionHistory.has("discovery_lesson")) {
                                    if (!progressionHistory.get("discovery_lesson").isJsonNull) {

                                        val progressionLevel11 =
                                            progressionHistory.get("discovery_lesson")
                                                .asJsonArray

                                        if (progressionLevel11.size() > 0) {

                                            for (i in 0 until progressionLevel11.size()) {
                                                if (!progressionLevel11.get(i).isJsonNull) {
                                                    val first =
                                                        progressionLevel11.get(i).asJsonObject
                                                    if (first.has("id")) {
                                                        progressionId =
                                                            CustomMethods().getString(first, "id")
                                                    }
                                                    if (first.has("status")) {

                                                        statusOne =
                                                            CustomMethods().getString(
                                                                first,
                                                                "status"
                                                            )
                                                    }
                                                    if (statusOne.equals("1")) {
                                                        status_typeOne = "true"
                                                    }
                                                    if (first.has("cost")) {

                                                        cost =
                                                            CustomMethods().getString(first, "cost")
                                                    }
                                                    if (first.has("type")) {
                                                        progressionType =
                                                            CustomMethods().getString(first, "type")
                                                    }
                                                    if (first.has("date")) {
                                                        date =
                                                            CustomMethods().getString(first, "date")
                                                    }
                                                    if (first.has("location_id")) {
                                                        locationId =
                                                            CustomMethods().getString(
                                                                first,
                                                                "location_id"
                                                            )
                                                    }
                                                    if (first.has("comment")) {
                                                        comment =
                                                            CustomMethods().getString(
                                                                first,
                                                                "comment"
                                                            )
                                                    }
                                                    if (first.has("commented_by")) {
                                                        commentedBy =
                                                            CustomMethods().getString(
                                                                first,
                                                                "commented_by"
                                                            )
                                                    }

                                                    if (first.has("payment")) {

                                                        if ((!first.get("payment").isJsonNull)) {
                                                            if ((first.get(
                                                                    "payment"
                                                                ).isJsonObject)
                                                            ) {
                                                                val payment =
                                                                    first.get("payment")
                                                                        .asJsonObject
                                                                pId = payment.get("id").asString
                                                                paymentModelId =
                                                                    payment.get("model_id").asString
                                                                paymentId =
                                                                    payment.get("payment_id")
                                                                        .asString
                                                            }
                                                        }

                                                    }

                                                    if (first.has("instructor")) {
                                                        if ((!first.get("instructor").isJsonNull)) {
                                                            if ((first.get(
                                                                    "instructor"
                                                                ).isJsonObject)
                                                            ) {
                                                                val instructor =
                                                                    first.get("instructor")
                                                                        .asJsonObject
                                                                instructorId =
                                                                    instructor.get("id").asString
                                                                instructorName =
                                                                    instructor.get("name").asString
                                                            }
                                                        }
                                                    }
                                                    if (first.has("location")) {
                                                        if ((!first.get("location").isJsonNull)) {
                                                            if ((first.get(
                                                                    "location"
                                                                ).isJsonObject)
                                                            ) {
                                                                val location =
                                                                    first.get("location")
                                                                        .asJsonObject
                                                                locId = location.get("id").asString
                                                                locName =
                                                                    location.get("name").asString
                                                            }
                                                        }
                                                    }

                                                    discoveryLession = DiscoveryLesson(
                                                        progressionId,
                                                        statusOne,
                                                        cost,
                                                        progressionType,
                                                        date,
                                                        locationId,
                                                        comment,
                                                        commentedBy,
                                                        pId,
                                                        paymentModelId,
                                                        paymentId,
                                                        instructorId,
                                                        instructorName,
                                                        locId,
                                                        locName,
                                                        status_typeOne
                                                    )

                                                }

                                                discoveryLessonList.add(discoveryLession as DiscoveryLesson)

                                            }

                                            recycler_progressionLevel_discovery.visibility =
                                                View.VISIBLE

                                            discoveryAdapter = DiscoveryAdapter(
                                                activity as MainActivity,
                                                discoveryLessonList, status_typeOne
                                            )
                                            recycler_progressionLevel_discovery.adapter =
                                                discoveryAdapter
                                            recycler_progressionLevel_discovery.layoutManager =
                                                LinearLayoutManager(context)
                                            val dividerItemDecoration = DividerItemDecoration(
                                                recycler_progressionLevel_discovery.context,
                                                DividerItemDecoration.VERTICAL
                                            )
                                            recycler_progressionLevel_discovery.addItemDecoration(
                                                dividerItemDecoration
                                            )

                                            discoveryAdapter.notifyDataSetChanged()


                                        }
                                    }
                                }

                                //Progression Level 1

                                if (progressionHistory.has("cruiser")) {
                                    if (!progressionHistory.get("cruiser").isJsonNull) {

                                        val progressionLevel11 =
                                            progressionHistory.get("cruiser")
                                                .asJsonArray

                                        if (progressionLevel11.size() > 0) {

                                            for (i in 0 until progressionLevel11.size()) {
                                                if (!progressionLevel11.get(i).isJsonNull) {
                                                    val first =
                                                        progressionLevel11.get(i).asJsonObject
                                                    if (first.has("id")) {
                                                        progressionId =
                                                            CustomMethods().getString(first, "id")
                                                    }
                                                    if (first.has("status")) {

                                                        status =
                                                            CustomMethods().getString(
                                                                first,
                                                                "status"
                                                            )
                                                    }
                                                    if (status.equals("1")) {
                                                        status_type = "true"
                                                    }
                                                    if (first.has("cost")) {

                                                        cost =
                                                            CustomMethods().getString(first, "cost")
                                                    }
                                                    if (first.has("type")) {
                                                        progressionType =
                                                            CustomMethods().getString(first, "type")
                                                    }
                                                    if (first.has("date")) {
                                                        date =
                                                            CustomMethods().getString(first, "date")
                                                    }
                                                    if (first.has("location_id")) {
                                                        locationId =
                                                            CustomMethods().getString(
                                                                first,
                                                                "location_id"
                                                            )
                                                    }
                                                    if (first.has("comment")) {
                                                        comment =
                                                            CustomMethods().getString(
                                                                first,
                                                                "comment"
                                                            )
                                                    }
                                                    if (first.has("commented_by")) {
                                                        commentedBy =
                                                            CustomMethods().getString(
                                                                first,
                                                                "commented_by"
                                                            )
                                                    }

                                                    if (first.has("payment")) {

                                                        if ((!first.get("payment").isJsonNull)) {
                                                            if ((first.get(
                                                                    "payment"
                                                                ).isJsonObject)
                                                            ) {
                                                                val payment =
                                                                    first.get("payment")
                                                                        .asJsonObject
                                                                pId = payment.get("id").asString
                                                                paymentModelId =
                                                                    payment.get("model_id").asString
                                                                paymentId =
                                                                    payment.get("payment_id")
                                                                        .asString
                                                            }
                                                        }

                                                    }

                                                    if (first.has("instructor")) {
                                                        if ((!first.get("instructor").isJsonNull)) {
                                                            if ((first.get(
                                                                    "instructor"
                                                                ).isJsonObject)
                                                            ) {
                                                                val instructor =
                                                                    first.get("instructor")
                                                                        .asJsonObject
                                                                instructorId =
                                                                    instructor.get("id").asString
                                                                instructorName =
                                                                    instructor.get("name").asString
                                                            }
                                                        }
                                                    }
                                                    if (first.has("location")) {
                                                        if ((!first.get("location").isJsonNull)) {
                                                            if ((first.get(
                                                                    "location"
                                                                ).isJsonObject)
                                                            ) {
                                                                val location =
                                                                    first.get("location")
                                                                        .asJsonObject
                                                                locId = location.get("id").asString
                                                                locName =
                                                                    location.get("name").asString
                                                            }
                                                        }
                                                    }

                                                    progressionLevel1 = ProgressionLevel1(
                                                        progressionId,
                                                        status,
                                                        cost,
                                                        progressionType,
                                                        date,
                                                        locationId,
                                                        comment,
                                                        commentedBy,
                                                        pId,
                                                        paymentModelId,
                                                        paymentId,
                                                        instructorId,
                                                        instructorName,
                                                        locId,
                                                        locName,
                                                        status_type
                                                    )

                                                }

                                                progressionLevel1List.add(progressionLevel1 as ProgressionLevel1)

                                            }

                                            recyclerProgressionlevel1.visibility = View.VISIBLE

                                            adapterLevel1 = ProgressionLevel1Adapter(
                                                activity as MainActivity,
                                                progressionLevel1List, status_type
                                            )
                                            recyclerProgressionlevel1.adapter = adapterLevel1
                                            recyclerProgressionlevel1.layoutManager =
                                                LinearLayoutManager(context)
                                            val dividerItemDecoration = DividerItemDecoration(
                                                recyclerProgressionlevel1.context,
                                                DividerItemDecoration.VERTICAL
                                            )
                                            recyclerProgressionlevel1.addItemDecoration(
                                                dividerItemDecoration
                                            )

                                            adapterLevel1.notifyDataSetChanged()


                                        }
                                    }
                                }

                                //Progression Level 2


                                if (progressionHistory.has("explorer")) {
                                    if (!progressionHistory.get("explorer").isJsonNull) {

                                        val progressionLevel21 =
                                            progressionHistory.get("explorer")
                                                .asJsonArray

                                        if (progressionLevel21.size() > 0) {

                                            for (i in 0 until progressionLevel21.size()) {
                                                if (!progressionLevel21.get(i).isJsonNull) {
                                                    val first =
                                                        progressionLevel21.get(i).asJsonObject
                                                    if (first.has("id")) {
                                                        progressionId =
                                                            CustomMethods().getString(first, "id")
                                                    }

                                                    if (first.has("status")) {

                                                        status2 =
                                                            CustomMethods().getString(
                                                                first,
                                                                "status"
                                                            )
                                                    }
                                                    if (status2.equals("1")) {
                                                        status_type2 = "true"
                                                    }
                                                    if (first.has("cost")) {

                                                        cost =
                                                            CustomMethods().getString(first, "cost")
                                                    }
                                                    if (first.has("type")) {
                                                        progressionType =
                                                            CustomMethods().getString(first, "type")
                                                    }
                                                    if (first.has("date")) {
                                                        date =
                                                            CustomMethods().getString(first, "date")
                                                    }
                                                    if (first.has("location_id")) {
                                                        locationId =
                                                            CustomMethods().getString(
                                                                first,
                                                                "location_id"
                                                            )
                                                    }
                                                    if (first.has("comment")) {
                                                        comment =
                                                            CustomMethods().getString(
                                                                first,
                                                                "comment"
                                                            )
                                                    }
                                                    if (first.has("commented_by")) {
                                                        commentedBy =
                                                            CustomMethods().getString(
                                                                first,
                                                                "commented_by"
                                                            )
                                                    }

                                                    if (first.has("payment")) {

                                                        if ((!first.get("payment").isJsonNull)) {
                                                            if ((first.get(
                                                                    "payment"
                                                                ).isJsonObject)
                                                            ) {
                                                                val payment =
                                                                    first.get("payment")
                                                                        .asJsonObject
                                                                pId = payment.get("id").asString
                                                                paymentModelId =
                                                                    payment.get("model_id").asString
                                                                paymentId =
                                                                    payment.get("payment_id")
                                                                        .asString
                                                            }
                                                        }

                                                    }

                                                    if (first.has("instructor")) {
                                                        if ((!first.get("instructor").isJsonNull)) {
                                                            if ((first.get(
                                                                    "instructor"
                                                                ).isJsonObject)
                                                            ) {
                                                                val instructor =
                                                                    first.get("instructor")
                                                                        .asJsonObject
                                                                instructorId =
                                                                    instructor.get("id").asString
                                                                instructorName =
                                                                    instructor.get("name").asString
                                                            }
                                                        }
                                                    }
                                                    if (first.has("location")) {
                                                        if ((!first.get("location").isJsonNull)) {
                                                            if ((first.get(
                                                                    "location"
                                                                ).isJsonObject)
                                                            ) {
                                                                val location =
                                                                    first.get("location")
                                                                        .asJsonObject
                                                                locId = location.get("id").asString
                                                                locName =
                                                                    location.get("name").asString
                                                            }
                                                        }
                                                    }

                                                    progressionLevel2 = ProgressionLevel2(
                                                        progressionId,
                                                        status2,
                                                        cost,
                                                        progressionType,
                                                        date,
                                                        locationId,
                                                        comment,
                                                        commentedBy,
                                                        pId,
                                                        paymentModelId,
                                                        paymentId,
                                                        instructorId,
                                                        instructorName,
                                                        locId,
                                                        locName,
                                                        status_type2
                                                    )

                                                }

                                                progressionLevel2List.add(progressionLevel2 as ProgressionLevel2)
                                            }

                                            tv_progression_title2.visibility = View.VISIBLE
                                            adapterLevel2 = ProgressionLevel2Adapter(
                                                activity as MainActivity,
                                                progressionLevel2List, status_type2, status
                                            )
                                            recyclerProgressionlevel2.adapter = adapterLevel2
                                            recyclerProgressionlevel2.layoutManager =
                                                LinearLayoutManager(context)
                                            val dividerItemDecoration = DividerItemDecoration(
                                                recyclerProgressionlevel2.context,
                                                DividerItemDecoration.VERTICAL
                                            )
                                            recyclerProgressionlevel2.addItemDecoration(
                                                dividerItemDecoration
                                            )

                                            adapterLevel2.notifyDataSetChanged()


                                        }
                                    }
                                }

                                //Progression Level 3

                                if (progressionHistory.has("sport")) {
                                    if (!progressionHistory.get("sport").isJsonNull) {

                                        val progressionLevel31 =
                                            progressionHistory.get("sport")
                                                .asJsonArray

                                        if (progressionLevel31.size() > 0) {

                                            for (i in 0 until progressionLevel31.size()) {
                                                if (!progressionLevel31.get(i).isJsonNull) {
                                                    val first =
                                                        progressionLevel31.get(i).asJsonObject
                                                    if (first.has("id")) {
                                                        progressionId =
                                                            CustomMethods().getString(first, "id")
                                                    }
                                                    if (first.has("status")) {

                                                        status3 =
                                                            CustomMethods().getString(
                                                                first,
                                                                "status"
                                                            )
                                                    }
                                                    if (status3.equals("1")) {
                                                        status_type3 = "true"
                                                    }
                                                    if (first.has("cost")) {

                                                        cost =
                                                            CustomMethods().getString(first, "cost")
                                                    }
                                                    if (first.has("type")) {
                                                        progressionType =
                                                            CustomMethods().getString(first, "type")
                                                    }
                                                    if (first.has("date")) {
                                                        date =
                                                            CustomMethods().getString(first, "date")
                                                    }
                                                    if (first.has("location_id")) {
                                                        locationId =
                                                            CustomMethods().getString(
                                                                first,
                                                                "location_id"
                                                            )
                                                    }
                                                    if (first.has("comment")) {
                                                        comment =
                                                            CustomMethods().getString(
                                                                first,
                                                                "comment"
                                                            )
                                                    }
                                                    if (first.has("commented_by")) {
                                                        commentedBy =
                                                            CustomMethods().getString(
                                                                first,
                                                                "commented_by"
                                                            )
                                                    }

                                                    if (first.has("payment")) {

                                                        if ((!first.get("payment").isJsonNull)) {
                                                            if ((first.get(
                                                                    "payment"
                                                                ).isJsonObject)
                                                            ) {


                                                                val payment =
                                                                    first.get("payment")
                                                                        .asJsonObject
                                                                pId = payment.get("id").asString
                                                                paymentModelId =
                                                                    payment.get("model_id").asString
                                                                paymentId =
                                                                    payment.get("payment_id")
                                                                        .asString
                                                            }
                                                        }

                                                    }

                                                    if (first.has("instructor")) {
                                                        if ((!first.get("instructor").isJsonNull)) {
                                                            if ((first.get(
                                                                    "instructor"
                                                                ).isJsonObject)
                                                            ) {
                                                                val instructor =
                                                                    first.get("instructor")
                                                                        .asJsonObject
                                                                instructorId =
                                                                    instructor.get("id").asString
                                                                instructorName =
                                                                    instructor.get("name").asString
                                                            }
                                                        }
                                                    }
                                                    if (first.has("location")) {
                                                        if ((!first.get("location").isJsonNull)) {
                                                            if ((first.get(
                                                                    "location"
                                                                ).isJsonObject)
                                                            ) {
                                                                val location =
                                                                    first.get("location")
                                                                        .asJsonObject
                                                                locId = location.get("id").asString
                                                                locName =
                                                                    location.get("name").asString
                                                            }
                                                        }
                                                    }

                                                    progressionLevel3 = ProgressionLevel3(
                                                        progressionId,
                                                        status3,
                                                        cost,
                                                        progressionType,
                                                        date,
                                                        locationId,
                                                        comment,
                                                        commentedBy,
                                                        pId,
                                                        paymentModelId,
                                                        paymentId,
                                                        instructorId,
                                                        instructorName,
                                                        locId,
                                                        locName,
                                                        status_type3
                                                    )

                                                }

                                                progressionLevel3List.add(progressionLevel3 as ProgressionLevel3)
                                            }


                                            tv_progression_title3.visibility = View.VISIBLE

                                            adapterLevel3 = ProgressionLevel3Adapter(
                                                activity as MainActivity,
                                                progressionLevel3List, status_type3, status2
                                            )
                                            recyclerProgressionlevel3.adapter = adapterLevel3
                                            recyclerProgressionlevel3.layoutManager =
                                                LinearLayoutManager(context)
                                            val dividerItemDecoration = DividerItemDecoration(
                                                recyclerProgressionlevel3.context,
                                                DividerItemDecoration.VERTICAL
                                            )
                                            recyclerProgressionlevel3.addItemDecoration(
                                                dividerItemDecoration
                                            )

                                            adapterLevel3.notifyDataSetChanged()


                                        }
                                    }
                                }

                                //Progression Level 4

                                if (progressionHistory.has("pro")) {
                                    if (!progressionHistory.get("pro").isJsonNull) {


                                        val progressionLevel41 =
                                            progressionHistory.get("pro")
                                                .asJsonArray


                                        if (progressionLevel41.size() > 0) {

                                            for (i in 0 until progressionLevel41.size()) {
                                                if (!progressionLevel41.get(i).isJsonNull) {
                                                    val first =
                                                        progressionLevel41.get(i).asJsonObject
                                                    if (first.has("id")) {
                                                        progressionId =
                                                            CustomMethods().getString(first, "id")
                                                    }
                                                    if (first.has("status")) {

                                                        status4 =
                                                            CustomMethods().getString(
                                                                first,
                                                                "status"
                                                            )
                                                    }
                                                    if (status4.equals("1")) {
                                                        status_type4 = "true"
                                                    }
                                                    if (first.has("cost")) {

                                                        cost =
                                                            CustomMethods().getString(first, "cost")
                                                    }
                                                    if (first.has("type")) {
                                                        progressionType =
                                                            CustomMethods().getString(first, "type")
                                                    }
                                                    if (first.has("date")) {
                                                        date =
                                                            CustomMethods().getString(first, "date")
                                                    }
                                                    if (first.has("location_id")) {
                                                        locationId =
                                                            CustomMethods().getString(
                                                                first,
                                                                "location_id"
                                                            )
                                                    }
                                                    if (first.has("comment")) {
                                                        comment =
                                                            CustomMethods().getString(
                                                                first,
                                                                "comment"
                                                            )
                                                    }
                                                    if (first.has("commented_by")) {
                                                        commentedBy =
                                                            CustomMethods().getString(
                                                                first,
                                                                "commented_by"
                                                            )
                                                    }

                                                    if (first.has("payment")) {

                                                        if ((!first.get("payment").isJsonNull)) {
                                                            if ((first.get(
                                                                    "payment"
                                                                ).isJsonObject)
                                                            ) {
                                                                val payment =
                                                                    first.get("payment")
                                                                        .asJsonObject
                                                                pId = payment.get("id").asString
                                                                paymentModelId =
                                                                    payment.get("model_id").asString
                                                                paymentId =
                                                                    payment.get("payment_id")
                                                                        .asString
                                                            }
                                                        }

                                                    }

                                                    if (first.has("instructor")) {
                                                        if ((!first.get("instructor").isJsonNull)) {
                                                            if ((first.get(
                                                                    "instructor"
                                                                ).isJsonObject)
                                                            ) {
                                                                val instructor =
                                                                    first.get("instructor")
                                                                        .asJsonObject
                                                                instructorId =
                                                                    instructor.get("id").asString
                                                                instructorName =
                                                                    instructor.get("name").asString
                                                            }
                                                        }
                                                    }
                                                    if (first.has("location")) {
                                                        if ((!first.get("location").isJsonNull)) {
                                                            if ((first.get(
                                                                    "location"
                                                                ).isJsonObject)
                                                            ) {
                                                                val location =
                                                                    first.get("location")
                                                                        .asJsonObject
                                                                locId = location.get("id").asString
                                                                locName =
                                                                    location.get("name").asString
                                                            }
                                                        }
                                                    }

                                                    progressionLevel4 = ProgressionLevel4(
                                                        progressionId,
                                                        status4,
                                                        cost,
                                                        progressionType,
                                                        date,
                                                        locationId,
                                                        comment,
                                                        commentedBy,
                                                        pId,
                                                        paymentModelId,
                                                        paymentId,
                                                        instructorId,
                                                        instructorName,
                                                        locId,
                                                        locName,
                                                        status_type4
                                                    )

                                                }
                                                progressionLevel4List.add(progressionLevel4 as ProgressionLevel4)


                                            }


                                            tv_progression_title4.visibility = View.VISIBLE

                                            adapterLevel4 = ProgressionLevel4Adapter(
                                                activity as MainActivity,
                                                progressionLevel4List, status_type4, status3
                                            )
                                            recyclerProgressionlevel4.adapter = adapterLevel4
                                            recyclerProgressionlevel4.layoutManager =
                                                LinearLayoutManager(context)
                                            val dividerItemDecoration = DividerItemDecoration(
                                                recyclerProgressionlevel4.context,
                                                DividerItemDecoration.VERTICAL
                                            )
                                            recyclerProgressionlevel4.addItemDecoration(
                                                dividerItemDecoration
                                            )

                                            adapterLevel4.notifyDataSetChanged()

                                        }
                                    }
                                }


                                //Certification

                                if (data.has("certification")) {
                                    if (!data.get("certification").isJsonNull) {
                                        val certification = data.get("certification").asJsonObject
//                                    val id = certification.get("id").asString
//                                    val type = certification.get("type").asString
//                                    val user_id = certification.get("user_id").asString
                                        val certificateDate =
                                            certification.get("certificate_date").asString
//                                    val certificate_by =
//                                        certification.get("certificate_by").asJsonObject
//                                    val idCertificate = certificate_by.get("id").asString
//                                    val name = certificate_by.get("name").asString
//                                    val cost = certification.get("cost").asString
//                                    val user = certification.get("user").asJsonObject
//                                    val idUser = user.get("id").asString
//                                    val nameUser = user.get("name").asString
                                        if (status.equals("1") && status2.equals("1")
                                            && status3.equals("1") && status4.equals("1")
                                        ) {
                                            linear_recycler_progression.visibility = View.VISIBLE
                                            tvProgressionmember.text =
                                                "Congratulations You are now Certified User"
                                            tv_progressionMember_membership_text.text =
                                                "to continue your sessions go to the locations tab and explore the world and set up discounted sessions"
                                            tvProgressionmember.setTextColor(
                                                ContextCompat.getColor(
                                                    context!!,
                                                    R.color.color_green
                                                )
                                            )
                                            tv_progressionMember_membership_text.setTextColor(
                                                ContextCompat.getColor(
                                                    context!!,
                                                    R.color.colorPrimaryDark
                                                )
                                            )
                                        } else {
                                            linear_recycler_progression.visibility = View.GONE
                                            tvProgressionmember.text =
                                                resources.getString(R.string.pending)
                                            tvProgressionmember.setTextColor(
                                                ContextCompat.getColor(
                                                    context!!,
                                                    R.color.color_red
                                                )
                                            )

                                        }

                                        val builderCertified = Dialog(context!!)
                                        val layoutInflater = LayoutInflater.from(context)
                                        val dialogCertified: View =
                                            layoutInflater.inflate(
                                                R.layout.certified_popup,
                                                null
                                            )

                                        val tvCertified =
                                            dialogCertified.findViewById<TextView>(R.id.tv_certified)
                                        val btnOk =
                                            dialogCertified.findViewById<TextView>(R.id.btn_ok)
                                        tvCertified.text = "You are now Certified as a Member"
                                        builderCertified.setContentView(dialogCertified)
                                        builderCertified.show()
                                        builderCertified.setCancelable(false)

                                        btnOk.setOnClickListener { builderCertified.dismiss() }


                                        val progression =
                                            Progression(
                                                progressionEnrollment as Progression_Enrollment,
                                                progressionLevel1 as ProgressionLevel1,
                                                progressionLevel2 as ProgressionLevel2,
                                                progressionLevel3 as ProgressionLevel3,
                                                progressionLevel4 as ProgressionLevel4
                                            )


                                    }
                                }

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


    private fun init() {
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.progressionFragment)
        activity!!.bottom_navigation.visibility = View.VISIBLE
        activity!!.back.visibility = View.GONE
        activity!!.menu.visibility = View.VISIBLE
        recyclerProgressionenrollment = view!!.findViewById(R.id.recycler_progressionEnrollment)
        recycler_progressionLevel_discovery =
            view!!.findViewById(R.id.recycler_progressionLevel_discovery)
        recyclerProgressionlevel1 = view!!.findViewById(R.id.recycler_progressionLevel1)
        recyclerProgressionlevel2 = view!!.findViewById(R.id.recycler_progressionLevel2)
        recyclerProgressionlevel3 = view!!.findViewById(R.id.recycler_progressionLevel3)
        recyclerProgressionlevel4 = view!!.findViewById(R.id.recycler_progressionLevel4)
        tvProgressionmember = view!!.findViewById(R.id.tv_progressionMember)
        tv_progressionMember_membership_text =
            view!!.findViewById(R.id.tv_progressionMember_membership_text)
        linear_progressionLevel1 = view!!.findViewById(R.id.linear_progressionLevel1)
        linear_progressionLevel2 = view!!.findViewById(R.id.linear_progressionLevel2)
        linear_progressionLevel3 = view!!.findViewById(R.id.linear_progressionLevel3)
        linear_progressionLevel4 = view!!.findViewById(R.id.linear_progressionLevel4)
        linear_recycler_progression = view!!.findViewById(R.id.linear_recycler_progression1)

        tv_progression_title1 = view!!.findViewById(R.id.tv_progression_title1)
        tv_progression_title2 = view!!.findViewById(R.id.tv_progression_title2)
        tv_progression_title3 = view!!.findViewById(R.id.tv_progression_title3)
        tv_progression_title4 = view!!.findViewById(R.id.tv_progression_title4)
        btn_book_now_first = view!!.findViewById(R.id.btn_book_now_first)
        btn_book_now_second = view!!.findViewById(R.id.btn_book_now_second)
        btn_book_now_third = view!!.findViewById(R.id.btn_book_now_third)
        btn_book_now_forth = view!!.findViewById(R.id.btn_book_now_forth)
    }
}
